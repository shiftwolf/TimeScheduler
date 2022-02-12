/** Event
 * Version: 1
 * 30.12.2021
 * Hendrik
 */

package com.example.timescheduler.Model;

import com.example.timescheduler.DeSerializer.CustomEventSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.Date;

@JsonSerialize(using = CustomEventSerializer.class)
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
    public User[] participants;
    private Date reminder;
    public AttachmentsInfo[] attachmentsInfo;

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
                ", participants=" + Arrays.toString(participants) +
                ", reminder=" + reminder +
                ", attachmentsInfo=" + Arrays.toString(attachmentsInfo) +
                '}';
    }

    public void setId(long id) {
        this.id = id;
    }
// Constructors

    public Event() {
    }
    public Event(long id) {
        this.id = id;
    }

    public Event(String name, Date date, Date duration, String location, priorities priority, User[] participantsEntities) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.priority = priority;
        this.participants = participantsEntities;
    }

    public Event(String name, Date date, Date duration, String location, String description, priorities priority, User[] participantsEntities, Date reminder) {
        this.name = name;
        this.date = date;
        this.duration = duration;
        this.location = location;
        this.description = description;
        this.priority = priority;
        this.participants = participantsEntities;
        this.reminder = reminder;
    }

    // Getters


    public AttachmentsInfo[] getAttachments() {
        return attachmentsInfo;
    }

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

    public User[] getParticipantsEntities() {
        return participants;
    }

    public Date getReminder() {
        return reminder;
    }

    // Setters


    public void setAttachments(AttachmentsInfo[] attachments) {
        this.attachmentsInfo = attachments;
    }

    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }

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
}
