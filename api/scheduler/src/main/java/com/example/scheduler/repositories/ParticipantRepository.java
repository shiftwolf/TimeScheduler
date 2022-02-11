package com.example.scheduler.repositories;

import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.ParticipantsEntityPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Special Interface for a CrudRepository that works on participant entities
 * Note: Implementations for this interface are automatically
 * generated by the Spring Framework. @Annotations should be used
 * for changing the implementation.
 */
public interface ParticipantRepository extends CrudRepository<ParticipantsEntity, ParticipantsEntityPK> {

    List<ParticipantsEntity> findAllByUserId(Long userId);
    List<ParticipantsEntity> findAllByEventId(Long eventId);
    @Query("SELECT participant.userId FROM ParticipantsEntity participant WHERE participant.eventId = :eventId")
    List<Long> findAllUserIdsByEventId(Long eventId);

}
