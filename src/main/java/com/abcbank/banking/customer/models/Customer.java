package com.abcbank.banking.customer.models;

import com.abcbank.banking.user.models.UserInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final Long serialVersionUID = 111L;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @JoinColumn(name = "user_id")
    @OneToOne
    private UserInfo user;

    @Column(name = "phone", unique = true)
    private String phone;

    private String address;

    private CustomerType type;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo userInfo) {
        this.user = userInfo;
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

    public CustomerType getPriority() {
        return type;
    }

    public void setPriority(CustomerType type) {
        this.type = type;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                '}';
    }

    public boolean isPriority() {
        return type.equals(CustomerType.PRIORITY);
    }
}
