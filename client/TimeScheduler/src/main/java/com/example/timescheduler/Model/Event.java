/* Event
 * Version: 1
 * 30.12.2021
 * Hendrik
 */

package com.example.timescheduler.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

    public static enum priorities {
        GREEN, YELLOW, RED
    }

    // Attributes
    private long id;
    private Date createdAt;
    private String name;
    private Date date;
    private Date duration;
    private String location;
    private String description;
    private priorities priority;
    public List<User> participantsEntities;


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", participantsEntities=" + participantsEntities +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }
// Constructors

    public Event() {
    }

    public Event(String name, Date date, Date duration, String location, priorities priority, List<User> participantsEntities) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.priority = priority;
        this.participantsEntities = participantsEntities;
    }

    public Event(long id, String name, Date date, Date duration, String location, String description, priorities priority, ArrayList<User> participants) {
        this.id = id;
        this.createdAt = new Date();
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.participantsEntities = participants;
    }

    // Getters
    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public Date getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public priorities getPriority() {
        return priority;
    }

    public List<User> getParticipantsEntities() {
        return participantsEntities;
    }

    // Setters
    public void setDate(Date date) {
        this.date = date;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(priorities priority) {
        this.priority = priority;
    }

    // Methods
    public void addParticipants(User newParticipant) {
        participantsEntities.add(newParticipant);
    }

}
