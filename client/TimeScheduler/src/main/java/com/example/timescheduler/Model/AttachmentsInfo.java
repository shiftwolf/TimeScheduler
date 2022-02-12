package com.example.timescheduler.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentsInfo {
    private Long id;
    private String filename;
    private Long eventId;

    @Override
    public String toString() {
        return "AttachmentsInfo{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", eventId=" + eventId +
                '}';
    }

    public AttachmentsInfo() {
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
