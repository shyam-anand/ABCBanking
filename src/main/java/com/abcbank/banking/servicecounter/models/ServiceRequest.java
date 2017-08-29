package com.abcbank.banking.servicecounter.models;

import com.abcbank.banking.customer.models.Customer;

import java.util.Date;
import java.util.List;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
public class ServiceRequest {

    private long id;
    private List<Service> services;
    private Customer customer;
    private Date createdOn = new Date();

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
