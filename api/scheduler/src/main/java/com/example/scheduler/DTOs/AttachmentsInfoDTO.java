package com.example.scheduler.DTOs;

public class AttachmentsInfoDTO {

    private Long id;
    private Long eventId;
    private String filename;

    public AttachmentsInfoDTO(Long id, Long eventId, String filename){
        this.id = id;
        this.eventId = eventId;
        this.filename = filename;
    }

    public Long getId() { return id;}

    public void setId(Long id) { this.id = id;}

    public Long getEventId() { return eventId;}

    public void setEventId(Long eventId) { this.eventId = eventId;}

    public String getFilename() { return filename;}

    public void setFilename(String filename) { this.filename = filename;}
}
