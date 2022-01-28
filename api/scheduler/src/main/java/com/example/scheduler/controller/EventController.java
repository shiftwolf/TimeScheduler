package com.example.scheduler.controller;

import com.example.scheduler.DTOs.EventDTO;
import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.TokensEntity;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.example.scheduler.repositories.TokenRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Entry-point for all event related REST-API requests
 */
@RestController
public class EventController {

    private final EventRepository eventRepository;
    private final TokenRepository tokenRepository;
    private final ParticipantRepository participantRepository;

    /**
     * Using constructor injection (Dependency injection)
     * @param eventRepository (injected)
     * @param tokenRepository (injected)
     * @param participantRepository (injected)
     */
    EventController(EventRepository eventRepository, TokenRepository tokenRepository, ParticipantRepository participantRepository) {
        this.eventRepository = eventRepository;
        this.tokenRepository = tokenRepository;
        this.participantRepository = participantRepository;
    }

    @GetMapping("/events")
    List<EventDTO> all() {
        //TODO: return event data from db
        return null;
    }


    @PostMapping("/events")
    void newEvent(@RequestBody EventDTO newEvent,
                  @RequestHeader("userId") Long userId,
                  @RequestHeader("token") String token) {


        TokensEntity tokensEntity = tokenRepository.findById(token).orElseThrow(LoginFailedException::new);

        if(!(tokensEntity.getUserId().longValue() == userId.longValue())) {
            throw new NoAuthorizationException(userId);
        }

        EventsEntity eventsEntity = new EventsEntity(
                newEvent.getName(),
                new Timestamp(newEvent.getDate()),
                new Timestamp(newEvent.getDuration()),
                newEvent.getLocation(), newEvent.getPriority()
        );

        eventRepository.save(eventsEntity);
        Long eventId = eventRepository.findTopByOrderByIdDesc().getId();
        participantRepository.save(new ParticipantsEntity(eventId, userId));

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
