package com.abcbank.banking.token.services;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.servicecounter.models.Service;
import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.token.models.Action;
import com.abcbank.banking.token.models.Status;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.repositories.TokenRepository;
import com.abcbank.banking.token.repositories.TokensCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         26/08/17
 */
@org.springframework.stereotype.Service
public class TokenServiceImpl implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    private final TokenRepository repository;
    private final TokensCache tokensCache;

    @Autowired
    public TokenServiceImpl(TokenRepository tokenRepository, TokensCache redisRepository) {
        repository = tokenRepository;
        tokensCache = redisRepository;
    }

    /**
     * Creates a new token and assigns it to the customer
     *
     * @param service  Service for which the token is requested
     * @param customer Customer
     * @return New Token if none exists for the Customer, or the existing token after updating
     */
    @Override
    public Token issue(Service service, Customer customer) {
        Token token = repository.findByCustomer(customer).stream()
                .filter(token1 -> !(token1.getStatus().equals(Status.COMPLETED) || token1.getStatus().equals(Status.CANCELLED)))
                .reduce((token1, token2) -> token2)
                .orElse(null);

        if (token != null && token.getServices().contains(service)) {
            logger.info("Token already issued for customer {} for service {}", customer.getId(), service.getId());
            return token;
        } else if (token == null) {
            logger.info("Issue new token for customer {} for service {}", customer.getId(), service.getId());
            token = new Token();
            token.setStatus(Status.CREATED);
            token.setCustomer(customer);
        }
        token.addService(service);
        token = save(token);
        // Cache the token
        tokensCache.save(token);
        logger.info("Token created. ID: {}", token.getId());
        return token;
    }

    @Override
    public Token get(long tokenId) {
        Token token = tokensCache.find(tokenId);
        if (token == null) {
            logger.debug("Cache miss for tokenId {}, checking database", tokenId);
            token = repository.findOne(tokenId);
            tokensCache.save(token);
        }
        return token;
    }

    @Override
    public Token getForCustomer(Customer customer, boolean includeCompleted) {
        Stream<Token> tokenStream = repository.findByCustomer(customer).stream();
        if (!includeCompleted)
            tokenStream = tokenStream.filter(token1 -> !(token1.getStatus().equals(Status.COMPLETED) || token1.getStatus().equals(Status.CANCELLED)));
        logger.debug("{} tokens found for customer {}", tokenStream.count(), customer.getId());
        return tokenStream.reduce((token1, token2) -> token2).orElse(null);
    }

    @Override
    public Page<Token> list(Pageable pageable, boolean includeCompleted) {
        if (includeCompleted)
            return repository.findAll(pageable);

        return repository.findByStatusNotIn(Arrays.asList(Status.CANCELLED, Status.COMPLETED), pageable);
    }

    public Token get(long tokenId, boolean refresh) {
        if (!refresh) return get(tokenId);
        else return repository.findOne(tokenId);
    }

    @Override
    public Token updateStatus(long tokenId, Status tokenStatus) {
        Token token = repository.findOne(tokenId);
        token.setStatus(tokenStatus);
        return repository.save(token);
    }

    @Override
    public Token addAction(long tokenId, Action action) {
        Token token = repository.findOne(tokenId);
        token.getActions().add(action);
        return repository.save(token);
    }

    @Override
    public Token queuedAt(Token token, ServiceCounter counter) {
        token.setStatus(Status.WAITING);
        token.setCounter(counter);
        logger.info("Token #{} is WAITING at counter #{}", token.getId(), counter.getId());
        return save(token);
    }

    private Token save(Token token) {
        logger.debug("Saving token #{} to database", token.getId());
        return repository.save(token);
    }
}
