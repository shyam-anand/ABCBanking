package com.abcbank.banking.customer.services;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.customer.models.CustomerDTO;
import com.abcbank.banking.token.models.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
public interface CustomersService {
    Customer create(CustomerDTO customer);

    Customer get(String id);

    Page<Customer> list(Pageable pageable);

    Token getToken(String customerId, boolean includeCompleted);
}
