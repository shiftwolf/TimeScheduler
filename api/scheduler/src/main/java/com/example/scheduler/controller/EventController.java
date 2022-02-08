package com.example.scheduler.controller;

import com.example.scheduler.DTOs.EventDTO;
import com.example.scheduler.entities.*;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.exceptions.UserNotFoundException;
import com.example.scheduler.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * @author Timo, Max
 * @version 1.0
 * Entry-point for all user related REST-API requests
 */
@RestController
public class EventController {

    private final EventRepository eventRepository;
    private final TokenRepository tokenRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ReminderRepository reminderRepository;

    /**
     * using constructor injection (Dependency injection)
     * meaning this constructor is typically not used manually.
     * @param eventRepository Repository that holds all events (injected)
     * @param tokenRepository Repository that holds all tokens of currently active users (injected)
     * @param participantRepository Repository that holds all participants of events (injected)
     * @param userRepository Repository that holds all registered users (injected)
     * @param reminderRepository Repository that holds all reminders (injected)
     */
    EventController(EventRepository eventRepository,
                    TokenRepository tokenRepository,
                    ParticipantRepository participantRepository,
                    UserRepository userRepository,
                    ReminderRepository reminderRepository) {
        this.eventRepository = eventRepository;
        this.tokenRepository = tokenRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.reminderRepository = reminderRepository;
    }
    /**
     ** Lists all events saved in the database and returns them
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return List of all Events registered in the database
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
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
    ResponseEntity<String> newEvent(@RequestBody EventDTO newEvent,
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

        reminderRepository.save(new RemindersEntity(eventId, new Timestamp(newEvent.getReminder())));

        return ResponseEntity.ok().body("Event: " + eventId +" created successfully");

    }

    /**
     * Edit Data of a specific event with the received data from the DTO
     * @param id of the event in the database
     * @param event DTO that holds all relevant data to edit the event
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the edit was successful
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
    @PutMapping("/events/id={id}")
    ResponseEntity<String> editEvent(@PathVariable Long id,
                                     @RequestBody EventDTO event,
                                     @RequestHeader("userId") Long userId,
                                     @RequestHeader("token") String token){
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}

        //Update the values saved in the eventEntity
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
        return ResponseEntity.ok().body("Event: " + id +" edited successfully");
    }
    /**
     * Add a participant Data of a specific event with the received data from the DTO
     * @param id of the event in the database
     * @param email of the participant you want to add
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the edit was successful
     * @throws UserNotFoundException if user can't be found in the database
     * @throws NoAuthorizationException if user authentication fails
     */
    @PutMapping("/events/id={id}/participants/email={email}")
    ResponseEntity<String> addParticipantsViaEmail(@PathVariable Long id,
                                     @PathVariable String email,
                                     @RequestHeader("userId") Long userId,
                                     @RequestHeader("token") String token)
                                    throws NoAuthorizationException,
                                    UserNotFoundException{
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}

        UsersEntity participant = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
        for (ParticipantsEntity e : participantRepository.findAllByEventId(id)){
            if(e.getUserId().equals(participant.getId())){
                return ResponseEntity.ok().body("User is already participating in the event");
            }
        }
        participantRepository.save(new ParticipantsEntity(id, participant.getId()));
        return ResponseEntity.ok().body("User: " + participant.getName() + " successfully added to the event");
    }

    /**
     * Get Data of a specific event by including his its id in the request url
     * @param id event id in the database
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     * @return DTO of the event with all information
     */
    @GetMapping("/events/id={id}")
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
     * Delete a specific event
     * @param id event id of the event you want to delete
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     */
    @DeleteMapping("/events/id={id}")
    ResponseEntity<String> deleteEvent(@PathVariable Long id,
                     @RequestHeader("userId") Long userId,
                     @RequestHeader("token") String token) {
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}
        eventRepository.deleteById(id);
        return ResponseEntity.ok().body("Event: " + id + "deleted successfully");
    }

    /**
     * Validates User to grant them access to Events, based on his admin role or his participation in the event
     * @param eventId of the event that the client wants to access
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     * @return <code>true</code> if the given userId and token match a tokenEntity in the database
     * and the accessing user is accessing an event he is participating or is an admin,
     * <code>false</code>  otherwise;
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
