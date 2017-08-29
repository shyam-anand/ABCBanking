package com.abcbank.banking.servicecounter.repositories;

import com.abcbank.banking.servicecounter.models.Service;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         26/08/17
 */
public interface ServiceRepository extends CrudRepository<Service, Long> {
}
