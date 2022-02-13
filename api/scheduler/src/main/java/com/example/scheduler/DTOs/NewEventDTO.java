package com.example.scheduler.DTOs;

import java.util.List;
/**
 * @author Max
 * @version 1.0
 * Data Transfer Object used to map Request Body JSON to, holds data to generate a new or edit an existing {@link com.example.scheduler.entities.EventsEntity}
 */
public class NewEventDTO {

    private Long id;
    private String name;

    private List<Long> participants;
    private Long date;
    private Long duration;
    private Long reminder;
    private String location;
    private Integer priority;

    NewEventDTO()  {}

    /**
     * normal constructor used to initialize the values
     * @param id database identifier of {@link com.example.scheduler.entities.EventsEntity}
     * @param name of the event
     * @param participants ids of users participating in the event {@link com.example.scheduler.DTOs.UserDTO}
     * @param date when the event takes place
     * @param duration how long the event will go
     * @param reminder when to send the participants a reminder Email
     * @param location where the event takes place
     * @param priority how urgent the Event is
     */
    public NewEventDTO(Long id, String name, List<Long> participants, Long date,
                       Long duration, Long reminder, String location, Integer priority){
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.date = date;
        this.duration = duration;
        this.reminder = reminder;
        this.location = location;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) { this.participants = participants;}

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getReminder() { return reminder;}

    public void setReminder(Long reminder) { this.reminder = reminder;}

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
}
