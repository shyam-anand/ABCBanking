package com.abcbank.banking.user.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         28/08/17
 */
@Entity
@Table(name = "user")
public class UserInfo implements Serializable {

    private static final Long serialVersionUID = 230L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    // Hashed password
    private String password;

    private String name;

    private UserType type;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

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
