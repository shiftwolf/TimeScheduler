package com.example.scheduler.DTOs;

import java.util.List;

public class EventDTO {

    private int id;
    private String name;

    private List<UserDTO> participants;
    private String date;
    private String duration;
    private String location;

    EventDTO()  {}

    public EventDTO(int id, String name, List<UserDTO> participants, String date, String duration, String location ){
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.date = date;
        this.duration = duration;
        this.location = location;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
