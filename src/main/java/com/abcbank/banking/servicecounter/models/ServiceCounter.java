package com.abcbank.banking.servicecounter.models;

import com.abcbank.banking.customer.models.CustomerType;
import com.abcbank.banking.token.models.Token;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Entity
@Table(name = "service_counter")
public class ServiceCounter implements Serializable, Comparable<ServiceCounter> {
    private static final Long serialVersionUID = 113L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private CustomerType type;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private Integer ordinal = 0;

    @OneToMany(mappedBy = "counter")
    private Collection<Token> tokenQueue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Collection<Token> getTokenQueue() {
        return tokenQueue;
    }

    public void setTokenQueue(Collection<Token> tokenQueue) {
        this.tokenQueue = tokenQueue;
    }

    @Override
    public int compareTo(ServiceCounter otherCounter) {
        int otherOrdinal = otherCounter.getOrdinal();
        CustomerType otherType = otherCounter.getType();

        if (otherOrdinal == ordinal && otherType == type) {
            return 0;
        } else if (otherCounter.getOrdinal() > ordinal) {
            return -1;
        } else return 1;
    }
}
