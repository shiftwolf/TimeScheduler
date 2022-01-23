package com.example.scheduler.controller;

import com.example.scheduler.DTOs.EventDTO;
import com.example.scheduler.repositories.EventRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Entry-point for all event related REST-API requests
 */
@RestController
public class EventController {

    private final EventRepository eventRepository;

    /**
     * @param eventRepository
     * using constructor injection (Dependency injection)
     * meaning this constructor is typically not used manually.
     */
    EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/events")
    List<EventDTO> all() {
        //TODO: return event data from db
        return null;
    }

    @PostMapping("/events")
    EventDTO newEvent(@RequestBody EventDTO newEvent) {
        //TODO: create new event data in db
        return null;
    }

    /**
     * @param id user's ID in the database
     * @return DTO of the database with all information
     */
    @GetMapping("/events/{id}")
    EventDTO one(@PathVariable Long id) {
        // TODO: get Event by ID
        return null;
    }

    /**
     * @param id event ID
     * delete a specific event
     */
    @DeleteMapping("/events/{id}")
    void deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
    }
}
