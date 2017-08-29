package com.abcbank.banking.user.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         28/08/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO implements Serializable {

    private static final Long serialVersionUID = 231L;

    private Long id;
    @JsonProperty("user_name")
    private String userName;
    private String password;
    private String name;
    private UserType type;
    private Date createdAt = new Date();

    public UserDTO(UserInfo userInfo) {
        id = userInfo.getId();
        userName = userInfo.getUserName();
        name = userInfo.getName();
        type = userInfo.getType();
        createdAt = userInfo.getCreatedAt();
    }

    public UserDTO() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
