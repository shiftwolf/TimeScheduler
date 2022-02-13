package com.example.scheduler.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Timo, Max
 * @version 1.0
 * JPA Repository Entity, this is the java representation of a sql table
 * Stores data on which user participates in which event
 */
@Entity
@Table(name = "participants", schema = "scheduler")
@IdClass(ParticipantsEntityPK.class)
public class ParticipantsEntity {

    @Basic
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    @JsonBackReference
    private EventsEntity events;

    public ParticipantsEntity() {

    }

    /**
     * normal constructor used to initialize the values
     * @param eventId database identifier of event the user is participating in
     * @param userId database identifier of the user participating in the event
     */
    public ParticipantsEntity(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public EventsEntity getEvents() {
        return events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantsEntity that = (ParticipantsEntity) o;
        return eventId == that.eventId && userId == that.userId && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, eventId, userId);
    }
}
