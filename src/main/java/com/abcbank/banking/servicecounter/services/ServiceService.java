package com.abcbank.banking.servicecounter.services;

import com.abcbank.banking.customer.models.CustomerType;
import com.abcbank.banking.servicecounter.models.Service;
import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.token.models.Token;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         26/08/17
 */
@org.springframework.stereotype.Service
public interface ServiceService {

    Collection<Service> listServices();

    Service findService(long id) throws EntityNotFoundException;

    ServiceCounter addCounter(long serviceId, CustomerType type);

    Token process(long serviceId, String customerId);

    Collection<ServiceCounter> getCounters(Long serviceId);
}
