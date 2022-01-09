package com.example.scheduler.repositories;

import com.example.scheduler.entities.EventsEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventsEntity, Long> {
}
