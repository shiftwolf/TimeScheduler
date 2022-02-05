package com.example.scheduler.controller;

import com.example.scheduler.DTOs.EventDTO;
import com.example.scheduler.entities.*;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.ParticipantRepository;
import com.example.scheduler.repositories.TokenRepository;
import com.example.scheduler.repositories.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * @param eventRepository Repository that holds all events (injected)
     * @param tokenRepository Repository that holds all tokens of currently active Users (injected)
     * @param participantRepository Repository that holds all participants of events (injected)
     * @param userRepository Repository that holds all registered Users (injected)
     * using constructor injection (Dependency injection)
     * meaning this constructor is typically not used manually.
     */
    EventController(EventRepository eventRepository,
                    TokenRepository tokenRepository,
                    ParticipantRepository participantRepository,
                    UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.tokenRepository = tokenRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/events")
    List<EventsEntity> all(@RequestHeader("userId") Long userId,
                           @RequestHeader("token") String token) {
        if(!tokenRepository.isValid(token, userId)){
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

        if(!tokenRepository.isValid(token, userId)){
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

        // Add all participants to the participants table
        participantRepository.save(new ParticipantsEntity(eventId, userId));
        for (Long id : newEvent.getParticipants()) {
            participantRepository.save(new ParticipantsEntity(eventId, id));
        }

    }

    @PutMapping("/events/{id}")
    void editEvent(@PathVariable Long id,
                   @RequestBody EventDTO event,
                  @RequestHeader("userId") Long userId,
                  @RequestHeader("token") String token){
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}

        EventsEntity eventsEntity = eventRepository.findById(id).orElseThrow(() ->new EventNotFoundException(id));
        eventsEntity.setName(event.getName());
        eventsEntity.setDate(new Timestamp(event.getDate()));
        eventsEntity.setDuration(new Timestamp(event.getDuration()));
        eventsEntity.setLocation(event.getLocation());
        eventsEntity.setPriority(event.getPriority());

        eventRepository.save(eventsEntity);

        // Delete all old participants
        participantRepository.deleteAll(participantRepository.findAllByEventId(id));

        // Add all new participants to the participants table
        participantRepository.save(new ParticipantsEntity(id, userId));
        for (Long participantsId : event.getParticipants()) {
            participantRepository.save(new ParticipantsEntity(id, participantsId));
        }

    }

    /**
     * @param id user's ID in the database
     * @return DTO of the database with all information
     */
    @GetMapping("/events/{id}")
    EventsEntity one(@PathVariable Long id,
                     @RequestHeader("userId") Long userId,
                    @RequestHeader("token") String token) {
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}

        EventsEntity eventsEntity = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        // Assert that event is actually associated to the user
        participantRepository.findById(new ParticipantsEntityPK(id, userId))
                .orElseThrow(() -> new EventNotFoundException(id));

        return eventsEntity;
    }

    /**
     * @param id event ID
     * delete a specific event
     */
    @DeleteMapping("/events/{id}")
    void deleteEvent(@PathVariable Long id,
                     @RequestHeader("userId") Long userId,
                     @RequestHeader("token") String token) {
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}
        eventRepository.deleteById(id);
    }

    /**
     * @param eventId of the event that the client wants to access
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     *  validates User to grant them access to Events, based on his admin role or his participation in the event
     */
    boolean validateParticipants(Long eventId, Long userId, String token){
        if(!tokenRepository.isValid(token, userId)){ return false;}

        if (eventRepository.findById(eventId).isPresent()){
            EventsEntity e = eventRepository.findById(eventId).get();
            for(ParticipantsEntity entity: e.getParticipantsEntities()){
                if(entity.getUserId().equals(userId)){return true;}
            }
        }
        if(userRepository.findById(userId).isPresent()){
            UsersEntity admin = userRepository.findById(userId).get();
            return admin.isAdmin();
        }
        return false;
    }
}
