package com.example.scheduler.repositories;

import com.example.scheduler.entities.AttachmentsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Special Interface for a CrudRepository that works on event entities
 * Note: Implementations for this interface are automatically
 * generated by the Spring Framework. @Annotations should be used
 * for changing the implementation.
 */
public interface AttachmentsRepository extends CrudRepository<AttachmentsEntity, Long> {

    @Query("SELECT e FROM AttachmentsEntity e WHERE e.eventId = :eventId")
    Iterable<AttachmentsEntity> findAttachmentsByEventId(@Param("eventId")Long eventId);
}
