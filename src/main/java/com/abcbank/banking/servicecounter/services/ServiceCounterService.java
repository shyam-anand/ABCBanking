package com.abcbank.banking.servicecounter.services;

import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.token.models.Token;

import java.util.Collection;
import java.util.List;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
public interface ServiceCounterService {

    ServiceCounter getCounter(long id);

    List<ServiceCounter> listCounters();

    Collection<Token> getTokenQueue(long id);

    Token advance(Long counterId);

    Token getCurrentToken(Long counterId);

    Token serve(Long counterId);
}
