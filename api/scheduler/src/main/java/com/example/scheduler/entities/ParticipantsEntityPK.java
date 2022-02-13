package com.example.scheduler.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Timo, Max
 * @version 1.0
 * JPA Repository Entity, this is the java representation of a combined Key of {@link com.example.scheduler.entities.ParticipantsEntity}
 */
public class ParticipantsEntityPK implements Serializable {

    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "user_id")
    private Long userId;

    public ParticipantsEntityPK() {

    }

    /**
     * normal constructor used to initialize the values
     * @param eventId database identifier of event the user is participating in
     * @param userId database identifier of the user participating in the event
     */
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
