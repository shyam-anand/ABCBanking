package com.abcbank.banking.token.models;

import com.abcbank.banking.customer.models.CustomerDTO;
import com.abcbank.banking.servicecounter.models.ServiceCounterDTO;
import com.abcbank.banking.servicecounter.models.ServiceDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO implements Serializable {

    @JsonIgnore
    private static final Logger logger = LoggerFactory.getLogger(TokenDTO.class);
    @JsonIgnore
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Long id;
    private CustomerDTO customer;
    private List<ServiceDTO> services = new ArrayList<>();
    private ServiceCounterDTO counter;
    private List<ActionDTO> actions;
    private Status status;
    private String createdAt;

    public TokenDTO() {
    }

    public TokenDTO(Token token) {
        id = token.getId();
        customer = new CustomerDTO(token.getCustomer());
        services = token.getServices().stream().map(ServiceDTO::new).collect(Collectors.toList());
        if (token.getCounter() != null)
            counter = new ServiceCounterDTO(token.getCounter());
        try {
            if (token.getActions() != null && token.getActions().size() > 0)
                actions = token.getActions().stream().map(ActionDTO::new).collect(Collectors.toList());
        } catch (LazyInitializationException e) {
            logger.warn(e.getMessage());
        }
        status = token.getStatus();
        createdAt = dateFormat.format(token.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<ServiceDTO> getServices() {
        return services;
    }

    public void setServices(List<ServiceDTO> services) {
        this.services = services;
    }

    public ServiceCounterDTO getCounter() {
        return counter;
    }

    public void setCounter(ServiceCounterDTO counter) {
        this.counter = counter;
    }

    public List<ActionDTO> getActions() {
        return actions;
    }

    public void setActions(List<ActionDTO> actions) {
        this.actions = actions;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = dateFormat.format(createdAt);
    }
}
