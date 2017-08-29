package com.abcbank.banking.servicecounter.models;

import com.abcbank.banking.customer.models.CustomerType;
import com.abcbank.banking.token.models.TokenDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceCounterDTO {
    private long id;
    private int ordinal;
    private ServiceDTO service;
    private CustomerType type;
    private List<TokenDTO> tokenQueue;

    public ServiceCounterDTO() {
    }

    public ServiceCounterDTO(ServiceCounter counter) {
        this.id = counter.getId();
        this.service = new ServiceDTO(counter.getService());
        this.ordinal = counter.getOrdinal();
        this.type = counter.getType();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public ServiceDTO getService() {
        return service;
    }

    public void setService(ServiceDTO service) {
        this.service = service;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public List<TokenDTO> getTokenQueue() {
        return tokenQueue;
    }

    public void setTokenQueue(List<TokenDTO> tokenQueue) {
        this.tokenQueue = tokenQueue;
    }
}
