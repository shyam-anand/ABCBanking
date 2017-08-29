package com.abcbank.banking.servicecounter.services;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.customer.models.CustomerType;
import com.abcbank.banking.customer.services.CustomersService;
import com.abcbank.banking.servicecounter.models.Service;
import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.servicecounter.repositories.ServiceCounterRepository;
import com.abcbank.banking.servicecounter.repositories.ServiceRepository;
import com.abcbank.banking.token.models.Status;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.services.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@org.springframework.stereotype.Service
public class CounterService implements ServiceCounterService, ServiceService {
    private static final Logger logger = LoggerFactory.getLogger(CounterService.class);

    private final ServiceCounterRepository counters;
    private final ServiceRepository services;

    private final TokenService tokens;
    private final CustomersService customers;

//    private final TokenQueue tokenQueue;

    @Autowired
    public CounterService(ServiceCounterRepository serviceCounterRepository,
                          ServiceRepository serviceRepository,
                          TokenService tokenService,
                          CustomersService customersService,
                          TokenQueue tokenQueue) {
        counters = serviceCounterRepository;
        services = serviceRepository;
        tokens = tokenService;
        customers = customersService;
//        this.tokenQueue = tokenQueue;
    }

    @Override
    public ServiceCounter getCounter(long id) {
        return counters.findOne(id);
    }

    @Override
    public List<ServiceCounter> listCounters() {
        List<ServiceCounter> serviceCounters = new ArrayList<>();
        counters.findAll().forEach(serviceCounters::add);

        return serviceCounters;
    }

    /**
     * Issues a new token to the service for the customer
     *
     * @param serviceId  ID of the Service for which token is requested
     * @param customerId Customer's ID
     * @return New Token if the request is new, or the existing token
     */
    @Override
    public Token process(long serviceId, String customerId) {
        logger.info("Processing service request from service {} from customer {}", serviceId, customerId);
        Service service = findService(serviceId);
        if (service == null) throw new IllegalArgumentException("Invalid service ID " + serviceId);

        Customer customer = customers.get(customerId);
        if (customer == null) throw new IllegalArgumentException("Invalid customer ID " + customerId);

        Token token = tokens.issue(service, customer);

        // If token has not been assigned to any counter
        if (token.getCounter() == null) {
            logger.info("Token #{} unassigned to service", token.getId());
            // Get first counter for token, and add the token to it's queue
            ServiceCounter firstCounter = getNextCounter(token.getId());
            assert firstCounter != null;
            logger.info("First counter for token #{} is #{}", token.getId(), firstCounter.getId());
            token = enqueue(firstCounter, token);
        }

        return token;
    }

    @Override
    public Collection<ServiceCounter> getCounters(Long serviceId) {
        return services.findOne(serviceId).getCounters();
    }

    /**
     * Move counter to the next token.
     *
     * @param counterId Counter ID
     * @return Next Token
     */
    @Override
    public Token advance(@NotNull Long counterId) {
        logger.info("Advancing counter #{}", counterId);
        ServiceCounter curCounter = counters.findOne(counterId);

        Queue<Token> tokenQueue = new LinkedBlockingQueue<>(getTokenQueue(counterId));
        final Token token = tokenQueue.poll();
        if (token == null) {
            logger.info("No more tokens for counter #{}", counterId);
            return null;
        }

        logger.debug("Current token at counter #{} is #{} in {}", counterId, token.getId(), token.getStatus());

        if (token.getStatus().equals(Status.WAITING) || token.getStatus().equals(Status.CREATED)) {
            logger.info("Moving token #{} to {} at counter #{}", token.getId(), Status.PROCESSING, counterId);
            tokens.updateStatus(token.getId(), Status.PROCESSING);
            return token;
        }

        Token nextToken;
        if (token.getStatus().equals(Status.PROCESSING)) {
            ServiceCounter nextCounter = getNextCounter(token.getId());
            if (nextCounter != null) {
                logger.info("Moving token #{} to next counter #{}", token.getId(), nextCounter.getId());
                enqueue(nextCounter, token);
            } else {
                logger.info("No more counters token #{}. Moving to {} at counter #{}", token.getId(), Status.COMPLETED, counterId);
                tokens.updateStatus(token.getId(), Status.COMPLETED);
            }
            nextToken = tokenQueue.peek();
        } else {
            do {
                tokenQueue.remove();
                nextToken = tokenQueue.peek();
            }
            while (nextToken != null && (nextToken.getStatus().equals(Status.WAITING) || nextToken.getStatus().equals(Status.CREATED)));
        }
        if (nextToken == null) {
            logger.info("No more tokens at counter #{}", counterId);
            return null;
        }

        logger.info("Next token for counter #{} is #{}", counterId, nextToken.getId());
        tokens.queuedAt(nextToken, curCounter);
        return nextToken;
    }

    /**
     * Get the Token being served currently
     *
     * @param counterId Counter ID
     * @return Token being served
     */
    @Override
    public Token getCurrentToken(Long counterId) {
        Collection<Token> tokenQueue = getTokenQueue(counterId);
        if (tokenQueue.isEmpty())
            return null;

        return tokenQueue.iterator().next();
    }

    /**
     * Move the current token from WAITING to PROCESSING
     *
     * @param counterId Counter ID
     * @return Token
     */
    @Override
    public Token serve(Long counterId) {
        ServiceCounter counter = counters.findOne(counterId);
        if (counter == null)
            throw new IllegalArgumentException("Invalid counter id " + counterId);

        Queue<Token> tokenQueue = new LinkedBlockingQueue<>(counter.getTokenQueue());
        Token token = tokenQueue.peek();
        if (token == null)
            throw new IllegalStateException("No tokens at counter #" + counterId);

        if (token.getStatus().equals(Status.WAITING)) {
            tokens.updateStatus(token.getId(), Status.PROCESSING);
        }

        return token;
    }

    /**
     * Get the Token queue for the counter
     *
     * @param counterId Counter ID
     * @return Token queue as a Collection
     */
    @Override
    public Collection<Token> getTokenQueue(@NotNull long counterId) {
        ServiceCounter counter = counters.findOne(counterId);
        if (counter == null)
            throw new IllegalArgumentException("No counter with id " + counterId);
        return counter.getTokenQueue();
    }

    /**
     * Add a token to the counter's queue
     *
     * @param counter Counter object
     * @param token   Token to be queued
     * @return Token, after queuing at the counter
     */
    private Token enqueue(@NotNull ServiceCounter counter, @NotNull Token token) {
        token = tokens.queuedAt(token, counter);
        logger.info("Token #{} queued at counter #{}", token.getId(), counter.getId());
        return token;
    }

    /**
     * Return the next counter for the token, from the list of pending counters of the service,
     * or from the list of pending services
     *
     * @param tokenId Token ID
     * @return ServiceCounter object, if exists, or null
     */
    private ServiceCounter getNextCounter(@NotNull final Long tokenId) {
        final Token token = tokens.get(tokenId, true);
        ServiceCounter currentCounter = token.getCounter();

        if (currentCounter != null) {
            logger.debug("Token #{} currently at counter #{}", tokenId, currentCounter.getId());
            Service currentService = currentCounter.getService();

            // All of the rest of the counters for current service
            SortedSet<ServiceCounter> pendingCounters = currentService.getCounters().tailSet(currentCounter);
            Iterator<ServiceCounter> serviceCounterIterator = pendingCounters.iterator();
            serviceCounterIterator.next(); // iterate once, to pass the current counter

            if (serviceCounterIterator.hasNext()) {
                logger.info("{} more counters for service #{}", (pendingCounters.size() - 1), currentService.getId());
                Integer currentOrdinal = currentCounter.getOrdinal();
                logger.info("Current ordinal is {}", currentOrdinal);
                return nextCounter(pendingCounters, currentOrdinal, token.getCustomer().getType());
            }

            // No more counters for current service, get next service
            Iterator<Service> serviceIterator = token.getServices().iterator();
            while (serviceIterator.hasNext() && !serviceIterator.next().equals(currentService)) {
            } // loop till current service
            if (serviceIterator.hasNext()) {
                currentService = serviceIterator.next();
                logger.info("Add counters for pending services for token #{}", token.getId());
                return nextCounter(currentService.getCounters(), null, token.getCustomer().getType());
            } else {
                // No more services
                logger.info("No more services for token #{}", token.getId());
                return null;
            }

        } else {
            // No counters assigned. Assign first counter
            logger.debug("Assigning first counter for token #{}", tokenId);
            return nextCounter(token.getServices().get(0).getCounters(), null, token.getCustomer().getType());
        }
    }

    /**
     * Return ServiceCounter from the passed set, matching the ordinal and priority
     *
     * @param countersSet Set of ServiceCounter objects to select from
     * @param ordinal     Current Ordinal for the ServiceCounter
     * @param type        CustomerType for the ServiceCounter
     * @return ServiceCounter
     */
    private ServiceCounter nextCounter(Collection<ServiceCounter> countersSet, Integer ordinal, CustomerType type) {
        ServiceCounter nextCounter;

        // Counters matching the ordinal
        Set<ServiceCounter> nextCounters = countersSet.stream()
                .filter(counter -> counter.getOrdinal() > ordinal)
                .collect(Collectors.toSet());

        if (nextCounters.size() == 0) {
            logger.info("No counters with ordinal > {}", ordinal);
            return null;
        }

        if (nextCounters.size() > 1) {
            logger.debug("{} counters with ordinal > {}. Finding {} counter", nextCounters.size(), ordinal, type);
            Iterator<ServiceCounter> counterIterator = nextCounters.iterator();
            do {
                nextCounter = counterIterator.next(); // get next counter
                logger.debug("Found {} counter (#{})", nextCounter.getType(), nextCounter.getId());
            } while (!nextCounter.getType().equals(type)); // while the counter type and customer type doesn't match
        } else {
            // There is only one counter with the ordinal
            logger.debug("1 counter with ordinal > {}", ordinal);
            nextCounter = nextCounters.iterator().next();
        }

        return nextCounter;
    }

    @Override
    public Collection<Service> listServices() {
        Collection<Service> servicesList = new ArrayList<>();
        services.findAll().forEach(servicesList::add);
        return servicesList;
    }

    @Override
    public Service findService(long id) {
        Service s = services.findOne(id);
        if (s != null) return s;
        else throw new EntityNotFoundException("No service with id " + id);
    }

    @Override
    public ServiceCounter addCounter(long serviceId, CustomerType type) {
        ServiceCounter counter = new ServiceCounter();
        counter.setType(type);

        Service service = findService(serviceId);
//        service.add(counter);

        Integer ordinal = 0;
        if (service.getCounters().size() > 0) {
            logger.debug("{} counters for service", service.getCounters().size());
            ordinal = (int) service.getCounters().stream()
                    .filter(counter1 -> {
                        logger.debug("{} ({}) equals {} {}", counter1.getId(), counter1.getType(), type, counter1.getType().equals(type));
                        return counter1.getType().equals(type);
                    })
                    .count();
            logger.debug("{} of type {}", ordinal, type);
        }
        logger.debug("Setting ordinal: " + ordinal);
        counter.setOrdinal(ordinal);
        counter.setService(service);

        return counters.save(counter);
    }
}
