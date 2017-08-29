package com.abcbank.banking.token.models;

import com.abcbank.banking.customer.models.Customer;
import com.abcbank.banking.servicecounter.models.Service;
import com.abcbank.banking.servicecounter.models.ServiceCounter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Entity
@Table(name = "token")
public class Token implements Serializable {

    private static final Long serialVersionUID = 115L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "customer_id")
    @OneToOne
    private Customer customer;

    @JoinTable
    @ManyToMany
    private List<Service> services = new ArrayList<>();

    @OneToMany(mappedBy = "token")
    private List<Action> actions;

    private Status status;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @ManyToOne
    private ServiceCounter counter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void addService(Service service) {
        getServices().add(service);
    }

    public ServiceCounter getCounter() {
        return counter;
    }

    public void setCounter(ServiceCounter counter) {
        this.counter = counter;
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", customer=" + customer +
                ", services=" + services +
                ", actions=" + actions +
                ", updateStatus=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

    public static Token fromDTO(TokenDTO tokenDTO) {
        Token token = new Token();
        if (tokenDTO.getId() != null)
            token.id = tokenDTO.getId();
        if (tokenDTO.getStatus() != null)
            token.status = tokenDTO.getStatus();

        return token;
    }
}
