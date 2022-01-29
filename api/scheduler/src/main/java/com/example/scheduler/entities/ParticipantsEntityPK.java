package com.example.scheduler.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParticipantsEntityPK implements Serializable {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    public ParticipantsEntityPK() {

    }

    public ParticipantsEntityPK(Long eventId, Long userId) {
        this.eventId = eventId;
        this.userId = userId;
    }


    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantsEntityPK that = (ParticipantsEntityPK) o;
        return eventId == that.eventId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }
}
