package com.example.scheduler.controller;

import com.example.scheduler.DTOs.AttachmentsInfoDTO;
import com.example.scheduler.entities.AttachmentsEntity;
import com.example.scheduler.entities.EventsEntity;
import com.example.scheduler.entities.ParticipantsEntity;
import com.example.scheduler.entities.UsersEntity;
import com.example.scheduler.exceptions.AttachmentNotFoundException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.exceptions.UploadFailedException;
import com.example.scheduler.repositories.AttachmentsRepository;
import com.example.scheduler.repositories.EventRepository;
import com.example.scheduler.repositories.TokenRepository;
import com.example.scheduler.repositories.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Max
 * @version 1.0
 * Entry-point for all attachment file related REST-API requests
 */
@RestController
public class AttachmentsController {

    private final AttachmentsRepository attachmentsRepository;
    private final TokenRepository tokenRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    /**
    * using constructor injection (Dependency injection)
    * meaning this constructor is typically not used manually.
    * @param attachmentsRepository Repository that holds all attachments (injected)
    * @param tokenRepository Repository that holds all tokens of currently active Users (injected)
    * @param eventRepository Repository that holds all events (injected)
    * @param userRepository Repository that holds all registered Users (injected)
    */
    AttachmentsController(AttachmentsRepository attachmentsRepository,
                          TokenRepository tokenRepository,
                          EventRepository eventRepository,
                          UserRepository userRepository) {
        this.attachmentsRepository = attachmentsRepository;
        this.tokenRepository = tokenRepository;
        this.eventRepository =  eventRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lists infos on the attachments of the event
     * @param eventId id of the event you want to get the info from
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return infos(id, name) of the attachments associated with the event
     * @throws NoAuthorizationException if user authentication fails
     */
    @GetMapping("/attachments/eventId={eventId}")
    List<AttachmentsInfoDTO> getInfoByEvent(@PathVariable Long eventId,
                                            @RequestHeader("userId") Long userId,
                                            @RequestHeader("token") String token)
            throws NoAuthorizationException{
        if(!validateParticipants(eventId, userId, token)){throw new NoAuthorizationException(userId);}
        List<AttachmentsEntity> attachments = (List<AttachmentsEntity>) attachmentsRepository.findAttachmentsByEventId(eventId);
        List<AttachmentsInfoDTO> infoDTOS = new ArrayList<>();
        for (AttachmentsEntity e: attachments) {
            infoDTOS.add(new AttachmentsInfoDTO(e.getId(), eventId, e.getName()));
        }
        return infoDTOS;
    }

    /**
     * Uploads a file to the database
     * @param eventId id of the event you want to add an attachment to
     * @param file File of unspecified type that is to be saved in the database
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return Http response if the upload was successful
     * @throws NoAuthorizationException if user authentication fails
     * @throws UploadFailedException if the file could not be saved
     */
    @PostMapping("/attachments/eventId={eventId}")
    ResponseEntity<String> upload(@PathVariable Long eventId,
                                  InputStream file,
                                  @RequestHeader("fileName") String name,
                                  @RequestHeader("userId") Long userId,
                                  @RequestHeader("token") String token)
            throws NoAuthorizationException,
            UploadFailedException, IOException {
        if(!validateParticipants(eventId, userId, token)){throw new NoAuthorizationException(userId);}

            attachmentsRepository.save(new AttachmentsEntity(eventId, name, file.readAllBytes()));
        //}
        //catch (Exception e){throw new UploadFailedException();}
        return ResponseEntity.ok().body("Event: " + eventId +": File uploaded successfully");
    }

    /**
     * Lets the client download an attachment
     * @param id of the attachment you want to download to
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return the attachment as a byte array resource and a header that holds its info
     * @throws NoAuthorizationException if user authentication fails
     * @throws AttachmentNotFoundException if an attachment with the given id is not found
     */
    @GetMapping("/attachments/id={id}")
    ResponseEntity<byte[]> download(@PathVariable Long id,
                                               @RequestHeader("userId") Long userId,
                                               @RequestHeader("token") String token)
            throws NoAuthorizationException,
            AttachmentNotFoundException{
        AttachmentsEntity e = attachmentsRepository.findById(id).orElseThrow(() -> new AttachmentNotFoundException(id));
        if(!validateParticipants(e.getEventId(), userId, token)){throw new NoAuthorizationException(userId);}

        byte[] r = e.getAttachment();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + e.getName() + "\"").body(r);
    }

    /**
     * Delete an attachment
     * @param id of the attachment you want to delete
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return if deletion was successful
     * @throws NoAuthorizationException if user authentication fails
     * @throws AttachmentNotFoundException if an attachment with the given id is not found
     */
    @DeleteMapping("/attachments/id={id}")
    ResponseEntity<String> delete(@PathVariable Long id,
                                    @RequestHeader("userId") Long userId,
                                    @RequestHeader("token") String token)
            throws NoAuthorizationException,
            AttachmentNotFoundException{
        AttachmentsEntity e = attachmentsRepository.findById(id).orElseThrow(() -> new AttachmentNotFoundException(id));
        if(!validateParticipants(e.getEventId(), userId, token)){throw new NoAuthorizationException(userId);}

        attachmentsRepository.delete(e);
        return ResponseEntity.ok().body("Attachment: " + id + " deleted successfully");
    }


    /**
     * Validates User to grant them access to Attachments, based on his admin role or his participation in the attachment is associated to
     * @param eventId of the event that the client wants to access
     * @param userId of the requesting user
     * @param token of the requesting user, used to validate his login status
     * @return <code>true</code> if the given userId and token match a tokenEntity in the database
     * and the accessing user is accessing an event he is participating or is an admin,
     * <code>false</code>  otherwise;
     */
    boolean validateParticipants(Long eventId, Long userId, String token){
        if(!tokenRepository.isValid(token, userId)){ return false;}
        if(userRepository.findById(userId).isPresent()){
            UsersEntity admin = userRepository.findById(userId).get();
            if(admin.isAdmin()){return true;}
        }
        if (eventRepository.findById(eventId).isPresent()){
            EventsEntity e = eventRepository.findById(eventId).get();
            for(ParticipantsEntity entity: e.getParticipantsEntities()){
                if(entity.getUserId().equals(userId)){return true;}
            }
        }
        return false;
    }
}
