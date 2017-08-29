package com.abcbank.banking.customer.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {

    private String id;
    @JsonProperty("user_name")
    private String userName;
    private String password;
    private String name;
    private String phone;
    private String address;
    private CustomerType type;

    public CustomerDTO() {
    }

    public CustomerDTO(String userName, String password, String name, String phone, String address, CustomerType type) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.type = type;
    }

    public CustomerDTO(Customer c) {
        id = c.getId();
        userName = c.getUser().getName();
        name = c.getUser().getName();
        phone = c.getPhone();
        address = c.getAddress();
        type = c.getType();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }
}
