package com.example.scheduler.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParticipantsEntityPK implements Serializable {

    @Id
    @Column(name = "event_id")
    private int eventId;

    @Id
    @Column(name = "user_id")
    private int userId;


    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
