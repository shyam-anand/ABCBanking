package com.abcbank.banking.customer.controllers;

import com.abcbank.banking.common.models.ApiResponse;
import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.customer.models.CustomerDTO;
import com.abcbank.banking.customer.services.CustomersService;
import com.abcbank.banking.token.models.Token;
import com.abcbank.banking.token.models.TokenDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CustomersService customers;

    @Autowired
    public CustomerController(CustomersService customersService) {
        this.customers = customersService;
    }

    @ApiOperation(value = "Create customer")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody CustomerDTO customer) {
        Customer createdCustomer = customers.create(customer);
        ApiResponse response = new ApiResponse<>(new CustomerDTO(createdCustomer));
        response.add(linkTo(methodOn(CustomerController.class).get(createdCustomer.getId())).withSelfRel());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get customer")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get(@PathVariable("id") String id) {
        Customer customer = customers.get(id);
        if (customer != null) {
            ApiResponse response = new ApiResponse<>(new CustomerDTO(customer));
            response.add(linkTo(methodOn(CustomerController.class).get(id)).withSelfRel());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("List all customers")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity list(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                               @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {

        page = (page == null) ? 0 : page;
        size = (size == null) ? 20 : size;

        Pageable pageable = new PageRequest(page, size);
        Page<Customer> customerPage = customers.list(pageable);
        page = customerPage.getNumber();
        int totalPages = customerPage.getTotalPages();

        ApiResponse response;
        if (customerPage.getContent().size() > 0) {
            response = new ApiResponse<>(customerPage.getContent());

            if (totalPages > 1) {
                if (page < totalPages - 1) {
                    response.add(linkTo(methodOn(CustomerController.class).list(page, size)).withRel("next"));
                    response.add(linkTo(methodOn(CustomerController.class).list(totalPages - 1, size)).withRel("last"));
                }
                if (page > 0) {
                    response.add(linkTo(methodOn(CustomerController.class).list(page - 1, size)).withRel("prev"));
                    response.add(linkTo(methodOn(CustomerController.class).list(0, size)).withRel("first"));
                }
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse<>(Collections.EMPTY_LIST), HttpStatus.NO_CONTENT);
        }
    }

    @ApiOperation("Get token for a customer")
    @RequestMapping(value = "{customerId}/token", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTokens(@PathVariable("customerId") String customerId,
                                    @RequestParam(value = "all", required = false, defaultValue = "false") String inclAll) {
        Token token = customers.getToken(customerId, (inclAll.equalsIgnoreCase("y") || inclAll.equalsIgnoreCase("yes") || inclAll.equalsIgnoreCase("true")));
        ApiResponse response;
        if (token != null)
            response = new ApiResponse<>(new TokenDTO(token));
        else response = new ApiResponse<>(Collections.EMPTY_LIST);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
