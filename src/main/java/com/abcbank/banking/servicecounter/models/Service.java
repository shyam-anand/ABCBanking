package com.abcbank.banking.servicecounter.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         25/08/17
 */
@Entity
@Table(name = "service")
public class Service implements Serializable {

    private static final Long serialVersionUID = 110L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    @OrderBy("ordinal")
    private SortedSet<ServiceCounter> counters = new TreeSet<>(ServiceCounter::compareTo);

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

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

    public SortedSet<ServiceCounter> getCounters() {
        return counters;
    }

    public void setCounters(SortedSet<ServiceCounter> counters) {
        this.counters = counters;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

//    public int add(ServiceCounter counter) {
//        int ordinal = (int) getCounters().stream()
//                .filter(counter1 -> counter1.getType().equals(counter.getType()))
//                .count();
//
//        counter.setOrdinal(ordinal);
//        counter.setService(this);
//        getCounters().add(counter);
//
//        return counter.getOrdinal();
//    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", counters=" + counters +
                ", createdAt=" + createdAt +
                '}';
    }
}
