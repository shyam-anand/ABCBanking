package com.abcbank.banking.customer.repositories;

import com.abcbank.banking.customer.models.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, String> {
}
