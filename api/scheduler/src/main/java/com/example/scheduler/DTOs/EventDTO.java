package com.example.scheduler.DTOs;

import java.util.List;

public class EventDTO {

    private Long id;
    private String name;

    private List<UserDTO> participants;
    private Long date;
    private Long duration;
    private Long reminder;
    private String location;
    private Integer priority;
    private List<AttachmentsInfoDTO> attachmentsInfo;

    EventDTO()  {}

    public EventDTO(Long id, String name, List<UserDTO> participants, Long date,
                       Long duration, Long reminder, String location, Integer priority, List<AttachmentsInfoDTO> attachmentsInfo){
        this.id = id;
        this.name = name;
        this.participants = participants;
        this.date = date;
        this.reminder = reminder;
        this.location = location;
        this.priority = priority;
        this.attachmentsInfo = attachmentsInfo;
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

    public Long getReminder() { return reminder;}

    public void setReminder(Long reminder) { this.reminder = reminder;}

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

    public List<AttachmentsInfoDTO> getAttachmentsInfo() { return attachmentsInfo;}

    public void setAttachmentsInfo(List<AttachmentsInfoDTO> attachmentsInfo) { this.attachmentsInfo = attachmentsInfo;}
}
