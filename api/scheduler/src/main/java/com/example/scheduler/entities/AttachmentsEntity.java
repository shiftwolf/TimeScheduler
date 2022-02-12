package com.example.scheduler.entities;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "attachments", schema = "scheduler")
public class AttachmentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "name")
    private String name;

    @Basic
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Basic
    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    public AttachmentsEntity(){}

    public AttachmentsEntity(Long eventId, String name, byte[] attachment) {
        this.eventId =eventId;
        this.name = name;
        this.attachment = attachment;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttachmentsEntity that = (AttachmentsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(createdAt, that.createdAt) && Arrays.equals(attachment, that.attachment);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, createdAt);
        result = 31 * result + Arrays.hashCode(attachment);
        return result;
    }
}
