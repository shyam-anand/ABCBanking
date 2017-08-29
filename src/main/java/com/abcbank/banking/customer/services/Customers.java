package com.abcbank.banking.customer.services;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.customer.models.CustomerDTO;
import com.abcbank.banking.customer.repositories.CustomerRepository;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.services.TokenService;
import com.abcbank.banking.user.models.UserInfo;
import com.abcbank.banking.user.models.UserDTO;
import com.abcbank.banking.user.models.UserType;
import com.abcbank.banking.user.servives.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Service
public class Customers implements CustomersService {

    private UserService userService;
    private CustomerRepository repository;
    private TokenService tokenService;

    @Autowired
    public Customers(UserService userService,
                     CustomerRepository customerRepository,
                     TokenService tokenService) {
        this.userService = userService;
        repository = customerRepository;
        this.tokenService = tokenService;
    }

    @Override
    public Customer create(CustomerDTO customerDTO) {
        UserDTO user = new UserDTO();
        user.setName(customerDTO.getName());
        user.setUserName(customerDTO.getUserName());
        user.setPassword(customerDTO.getPassword());
        user.setType(UserType.CUSTOMER);
        UserInfo u = userService.create(user);

        Customer customer = new Customer();
        customer.setUser(u);
        customer.setAddress(customerDTO.getAddress());
        customer.setPhone(customerDTO.getPhone());
        customer.setPriority(customerDTO.getType());

        return repository.save(customer);
    }

    @Override
    public Customer get(String id) {
        return repository.findOne(id);
    }

    @Override
    public Page<Customer> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Token getToken(String customerId, boolean includeCompleted) {
        Customer customer = repository.findOne(customerId);
        return tokenService.getForCustomer(customer, includeCompleted);
    }
}
