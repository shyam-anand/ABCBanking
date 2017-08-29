package com.abcbank.banking.servicecounter.repositories;

import com.abcbank.banking.servicecounter.models.ServiceCounter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Repository
public interface ServiceCounterRepository extends CrudRepository<ServiceCounter, Long> {
}
