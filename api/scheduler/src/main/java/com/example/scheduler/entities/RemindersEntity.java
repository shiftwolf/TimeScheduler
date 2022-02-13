package com.example.scheduler.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Timo, Max
 * @version 1.0
 * JPA Repository Entity, this is the java representation of a sql table
 * Stores data of scheduled reminders, to remind users of upcoming events
 * Stores data if Reminder was sent
 */
@Entity
@Table(name = "reminders", schema = "scheduler")
public class RemindersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "date")
    private Timestamp date;

    @Basic
    @Column(name = "event_id")
    private Long eventId;

    @Basic
    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    public RemindersEntity(){}

    /**
     * normal constructor used to initialize the values
     * @param eventId database identifier of event the reminder is linked to
     * @param date on when the reminder should be sent
     */
    public RemindersEntity(Long eventId, Timestamp date){
        this.eventId = eventId;
        this.date = date;
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


    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


    public Long getEventId() { return eventId;}

    public void setEventId(Long eventId) { this.eventId = eventId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RemindersEntity that = (RemindersEntity) o;
        return id == that.id && Objects.equals(createdAt, that.createdAt) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, date);
    }


    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }
}
