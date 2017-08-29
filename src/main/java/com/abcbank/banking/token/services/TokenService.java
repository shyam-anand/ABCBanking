package com.abcbank.banking.token.services;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.servicecounter.models.Service;
import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.token.models.Action;
import com.abcbank.banking.token.models.Status;
import com.abcbank.banking.token.models.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
public interface TokenService {

    Token issue(Service service, Customer customer);

    Token get(long tokenId);

    Token get(long tokenId, boolean refresh);

    Token getForCustomer(Customer customer, boolean includeCompleted);

    Page<Token> list(Pageable pageable, boolean includeCompleted);

    Token updateStatus(long tokenId, Status tokenStatus);

    Token addAction(long tokenId, Action action);

    Token queuedAt(Token token, ServiceCounter counter);
}
