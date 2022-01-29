package com.example.scheduler.controller;

import com.example.scheduler.DTOs.EventDTO;
import com.example.scheduler.entities.*;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.exceptions.LoginFailedException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.example.scheduler.repositories.TokenRepository;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    List<EventsEntity> all(@RequestHeader("userId") Long userId,
                                      @RequestHeader("token") String token) {
        TokensEntity tokensEntity = tokenRepository.findById(token).orElseThrow(LoginFailedException::new);
        if(!(tokensEntity.getUserId().longValue() == userId.longValue())) {
            throw new NoAuthorizationException(userId);
        }


        List<ParticipantsEntity> participantsEntities = participantRepository
                .findAllByUserId(userId);

        List<EventsEntity> eventsEntityList = new ArrayList<>();

        for(ParticipantsEntity e : participantsEntities) {

            // Assert that event is actually associated to the user
            boolean eventBelongsToUser = participantRepository
                    .findById(new ParticipantsEntityPK(e.getEventId(), userId))
                    .isPresent();

            Optional<EventsEntity> eventsEntity = eventRepository.findById(e.getEventId());

            if (eventsEntity.isPresent() && eventBelongsToUser) {
                eventsEntityList.add(eventsEntity.orElseThrow(() -> new EventNotFoundException(e.getEventId())));
            }
        }
        
        return eventsEntityList;
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
    EventsEntity one(@PathVariable Long id, @RequestHeader("userId") Long userId,
                    @RequestHeader("token") String token) {
        TokensEntity tokensEntity = tokenRepository.findById(token).orElseThrow(LoginFailedException::new);
        if(!(tokensEntity.getUserId().longValue() == userId.longValue())) {
            throw new NoAuthorizationException(userId);
        }

        EventsEntity eventsEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        // Assert that event is actually associated to the user
        participantRepository.findById(new ParticipantsEntityPK(id, userId))
                .orElseThrow(() -> new EventNotFoundException(id));

//        System.out.println(
//        ReflectionToStringBuilder.toString(eventsEntity, new RecursiveToStringStyle())
//        );

        return eventsEntity;
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
