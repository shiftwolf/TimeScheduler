package com.example.scheduler.DTOs;

import java.util.List;

public class EventDTO {

    private int id;
    private String name;

    private List<UserDTO> participants;
    private Long date;
    private Long duration;
    private String location;
    private Integer priority;

    EventDTO()  {}

    public EventDTO(int id, String name, List<UserDTO> participants, Long date,
                    Long duration, String location, Integer priority){
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserDTO> participants) { this.participants = participants;}

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
