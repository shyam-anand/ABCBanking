package com.abcbank.banking.token.repositories;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.token.models.Status;
import com.abcbank.banking.token.models.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         26/08/17
 */
public interface TokenRepository extends PagingAndSortingRepository<Token, Long> {
    Collection<Token> findByCustomer(Customer customer);

    Page<Token> findByStatusNotIn(Collection<Status> statuses, Pageable pageable);
}
