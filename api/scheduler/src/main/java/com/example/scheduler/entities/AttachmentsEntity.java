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
    @Column(name = "id")
    private int id;

    @Basic
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "attachment")
    private byte[] attachment;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return id == that.id && Objects.equals(createdAt, that.createdAt) && Arrays.equals(attachment, that.attachment);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, createdAt);
        result = 31 * result + Arrays.hashCode(attachment);
        return result;
    }
}
