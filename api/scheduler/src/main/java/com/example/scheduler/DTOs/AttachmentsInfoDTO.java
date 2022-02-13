package com.example.scheduler.DTOs;

/**
 * @author Max
 * @version 1.0
 * Data Transfer Object used to generate Response Body to hold Info about an {@link com.example.scheduler.entities.AttachmentsEntity}
 */
public class AttachmentsInfoDTO {

    private Long id;
    private Long eventId;
    private String filename;

    /**
     * normal constructor used to initialize the values
     * @param id database identifier of {@link com.example.scheduler.entities.AttachmentsEntity}
     * @param eventId id of the event the attachment is linked to
     * @param filename of the attachment
     */
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
