package com.abcbank.banking.servicecounter.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDTO {

    private long id;
    private String title;
    private String description;

    public ServiceDTO(Service service) {
        id = service.getId();
        title = service.getTitle();
        description = service.getDescription();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
