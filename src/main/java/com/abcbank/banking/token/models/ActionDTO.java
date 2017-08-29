package com.abcbank.banking.token.models;

import com.abcbank.banking.servicecounter.models.ServiceCounterDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActionDTO implements Serializable {

    private long id;
    private String title;
    private String comment;
    private Status status;
    private ServiceCounterDTO author;
    private Date createdAt = new Date();

    public ActionDTO() {
    }

    public ActionDTO(Action action) {
        id = action.getId();
        title = action.getTitle();
        comment = action.getComment();
        status = action.getStatus();
        author = new ServiceCounterDTO(action.getCounter());
        createdAt = action.getCreatedAt();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public ServiceCounterDTO getAuthor() {
        return author;
    }

    public void setAuthor(ServiceCounterDTO author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
