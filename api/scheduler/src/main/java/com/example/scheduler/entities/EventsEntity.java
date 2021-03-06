package com.example.scheduler.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Timo, Max
 * @version 1.0
 * JPA Repository Entity, this is the java representation of a sql table
 * Stores Event data
 */
@Entity
@Table(name = "events", schema = "scheduler")
public class EventsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "date")
    private Timestamp date;

    @Basic
    @Column(name = "duration")
    private Timestamp duration;

    @Basic
    @Column(name = "location")
    private String location;

    @Basic
    @Column(name = "priority")
    private Integer priority;

    @OneToMany(
            mappedBy = "events",
            orphanRemoval = true
    )
    @JsonManagedReference
    private final List<ParticipantsEntity> participantsEntities = new ArrayList<>();

    public EventsEntity() {}

    /**
     * normal constructor used to initialize the values
     * @param name of the event
     * @param date when the event takes place
     * @param duration how long the event will go
     * @param location where the event takes place
     * @param priority how urgent the Event is
     */
    public EventsEntity(String name, Timestamp date, Timestamp duration,
                 String location, Integer priority) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.priority = priority;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() { return date;}

    public void setDate(Timestamp date) { this.date = date;}


    public Timestamp getDuration() {
        return duration;
    }

    public void setDuration(Timestamp duration) {
        this.duration = duration;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventsEntity that = (EventsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) && Objects.equals(name, that.name) && Objects.equals(duration, that.duration) && Objects.equals(location, that.location) && Objects.equals(priority, that.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, name, duration, location, priority);
    }

    public List<ParticipantsEntity> getParticipantsEntities() {
        return participantsEntities;
    }
}
