package com.abcbank.banking.token.models;

import com.abcbank.banking.servicecounter.models.ServiceCounter;
import com.abcbank.banking.user.models.UserInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Entity
@Table(name = "token_action")
public class Action implements Serializable {

    private static final Long serialVersionUID = 116L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "token_id")

    private Token token;
    private String title;
    private String comment;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "counter_id")
    private ServiceCounter counter;

    private UserInfo author;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ServiceCounter getCounter() {
        return counter;
    }

    public void setCounter(ServiceCounter counter) {
        this.counter = counter;
    }

    public UserInfo getAuthor() {
        return author;
    }

    public void setAuthor(UserInfo author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", token=" + token +
                ", title='" + title + '\'' +
                ", comment='" + comment + '\'' +
                ", updateStatus=" + status +
                ", author=" + counter +
                ", createdAt=" + createdAt +
                '}';
    }
}
