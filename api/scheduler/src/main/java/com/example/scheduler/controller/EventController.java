package com.example.scheduler.controller;

import com.example.scheduler.DTOs.AttachmentsInfoDTO;
import com.example.scheduler.DTOs.EventDTO;
import com.example.scheduler.DTOs.NewEventDTO;
import com.example.scheduler.DTOs.UserDTO;
import com.example.scheduler.entities.*;
import com.example.scheduler.exceptions.EventNotFoundException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.exceptions.UserNotFoundException;
import com.example.scheduler.repositories.*;
import com.example.scheduler.util.JavaMailUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private final AttachmentsRepository attachmentsRepository;
    private final JavaMailUtil mailUtil;

    /**
     * using constructor injection (Dependency injection)
     * meaning this constructor is typically not used manually.
     * @param eventRepository Repository that holds all events (injected)
     * @param tokenRepository Repository that holds all tokens of currently active users (injected)
     * @param participantRepository Repository that holds all participants of events (injected)
     * @param userRepository Repository that holds all registered users (injected)
     * @param reminderRepository Repository that holds all reminders (injected)
     * @param attachmentsRepository Repository that holds all event attachments (injected)
     * @param mailUtil Utility class used to send mails (injected)
     */
    EventController(EventRepository eventRepository,
                    TokenRepository tokenRepository,
                    ParticipantRepository participantRepository,
                    UserRepository userRepository,
                    ReminderRepository reminderRepository,
                    AttachmentsRepository attachmentsRepository,
                    JavaMailUtil mailUtil) {
        this.eventRepository = eventRepository;
        this.tokenRepository = tokenRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.reminderRepository = reminderRepository;
        this.attachmentsRepository = attachmentsRepository;
        this.mailUtil = mailUtil;
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
    List<EventDTO> all(@RequestHeader("userId") Long userId,
                       @RequestHeader("token") String token) {
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }

        List<EventDTO> dTOs = new ArrayList<>();
        // For all events the user is participating in
        for(ParticipantsEntity e :participantRepository.findAllByUserId(userId)) {

            EventsEntity event = eventRepository.findById(e.getEventId()).orElseThrow(() -> new EventNotFoundException(e.getEventId()));
            //Find users participating in the Event
            List<UserDTO> participants = new ArrayList<>();
            for (UsersEntity usersEntity: userRepository.findAllById(participantRepository.findAllUserIdsByEventId(event.getId()))){
                participants.add(new UserDTO(usersEntity.getId(),usersEntity.getUsername(), usersEntity.getName(),usersEntity.getEmail(), usersEntity.isAdmin()));
            }
            //Find info on the files attached to the event
            List<AttachmentsInfoDTO> infoDTOS = new ArrayList<>();
            for (AttachmentsEntity attachment: attachmentsRepository.findAttachmentsByEventId(event.getId())) {
                infoDTOS.add(new AttachmentsInfoDTO(attachment.getId(), attachment.getEventId(), attachment.getName()));
            }
            RemindersEntity reminder = reminderRepository.findByEventId(event.getId());
            dTOs.add(new EventDTO(event.getId(),
                    event.getName(),
                    participants,
                    event.getDate().getTime(),
                    event.getDuration().getTime(),
                    reminder.getDate().getTime(),
                    event.getLocation(),
                    event.getPriority(),infoDTOS)
            );
        }
        return dTOs;
    }

    @PostMapping("/events")
    ResponseEntity<String> newEvent(@RequestBody NewEventDTO newEvent,
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
        eventsEntity = eventRepository.save(eventsEntity);
        Long eventId = eventRepository.findTopByOrderByIdDesc().getId();

        // Add all participants to the participants table
        UsersEntity creator = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        participantRepository.save(new ParticipantsEntity(eventId, userId));

        for (Long id : newEvent.getParticipants()) {
            UsersEntity participant = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
            try {
                mailUtil.sendMail(participant.getEmail(),
                        "You have been added to Event: " + eventsEntity.getName(),
                        "Hello " + participant.getName() + ", \n"
                                + "You have been invited to participate in the Event "
                                + "\"" + eventsEntity.getName() + "\" at the following date: "
                                + eventsEntity.getDate().toString() +" and location: "
                                + eventsEntity.getLocation() + "by "
                                + creator.getName() +"! ");
            }catch (Exception e){
                return ResponseEntity.internalServerError().body("Messaging Error occurred: " + e.getMessage());
            }
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
                                     @RequestBody NewEventDTO event,
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
            UsersEntity participant = userRepository.findById(participantsId).orElseThrow(()-> new UserNotFoundException(participantsId));
            try {
                mailUtil.sendMail(participant.getEmail(),
                        "Event: " + eventsEntity.getName() +" has been edited",
                        "Hello " + participant.getName() + ", \n" + "The Event you are participating in has been edited! ");
            }catch (Exception e){
               return ResponseEntity.internalServerError().body("Messaging Error occurred: " + e.getMessage());
            }
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
        EventsEntity event = eventRepository.findById(id).orElseThrow(()-> new EventNotFoundException(id));
        UsersEntity creator = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));

        for (ParticipantsEntity e : participantRepository.findAllByEventId(id)){
            if(e.getUserId().equals(participant.getId())){
                return ResponseEntity.ok().body("User is already participating in the event");
            }
        }
        participantRepository.save(new ParticipantsEntity(id, participant.getId()));
        try {
            mailUtil.sendMail(participant.getEmail(),
                    "You have been added to Event: " + event.getName(),
                    "Hello " + participant.getName() + ", \n"
                            + "You have been invited to participate in the scheduled Event "
                            + "\"" + event.getName() + "\" at the following date: "
                            + event.getDate().toString() +" and location: "
                            + event.getLocation() + "by "
                            + creator.getName() +"! ");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Messaging Error occurred: " + e.getMessage());
        }
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
    EventDTO one(@PathVariable Long id,
                     @RequestHeader("userId") Long userId,
                    @RequestHeader("token") String token) {
        if(!validateParticipants(id,userId,token)){throw new NoAuthorizationException(userId);}

        EventsEntity event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        //Find users participating in the Event
        List<UserDTO> participants = new ArrayList<>();
        for (UsersEntity usersEntity: userRepository.findAllById(participantRepository.findAllUserIdsByEventId(event.getId()))){
            participants.add(new UserDTO(usersEntity.getId(),usersEntity.getUsername(), usersEntity.getName(),usersEntity.getEmail(), usersEntity.isAdmin()));
        }
        //Find info on the files attached to the event
        List<AttachmentsInfoDTO> infoDTOS = new ArrayList<>();
        for (AttachmentsEntity attachment: attachmentsRepository.findAttachmentsByEventId(event.getId())) {
            infoDTOS.add(new AttachmentsInfoDTO(attachment.getId(), attachment.getEventId(), attachment.getName()));
        }
        RemindersEntity reminder = reminderRepository.findByEventId(event.getId());
       return new EventDTO(event.getId(),
                event.getName(),
                participants,
                event.getDate().getTime(),
                event.getDuration().getTime(),
               reminder.getDate().getTime(),
                event.getLocation(),
                event.getPriority(),infoDTOS
       );
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
        EventsEntity event = eventRepository.findById(id).orElseThrow(()-> new EventNotFoundException(id));
        UsersEntity creator = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        for (ParticipantsEntity entity: participantRepository.findAllByEventId(id)){
            UsersEntity participant = userRepository.findById(entity.getUserId()).orElseThrow(() -> new UserNotFoundException(entity.getUserId()));
            try {
                mailUtil.sendMail(participant.getEmail(),
                        "Event: " + event.getName() + " has been deleted!",
                        "Hello " + participant.getName() + ", \n"
                                + "The Event you are participating in has been deleted by "
                                + creator.getName() +"! ");
            }catch (Exception e){
                return ResponseEntity.internalServerError().body("Messaging Error occurred: " + e.getMessage());
            }
        }
        eventRepository.delete(event);
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
