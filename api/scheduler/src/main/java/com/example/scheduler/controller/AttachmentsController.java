package com.example.scheduler.controller;

import com.example.scheduler.DTOs.AttachmentsInfoDTO;
import com.example.scheduler.entities.AttachmentsEntity;
import com.example.scheduler.exceptions.AttachmentNotFoundException;
import com.example.scheduler.exceptions.NoAuthorizationException;
import com.example.scheduler.exceptions.UploadFailedException;
import com.example.scheduler.repositories.TokenRepository;
import  com.example.scheduler.repositories.AttachmentsRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AttachmentsController {

    private final AttachmentsRepository attachmentsRepository;
    private final TokenRepository tokenRepository;

    /**
    * @param attachmentsRepository Repository that holds all attachments
    *  @param tokenRepository Repository that holds all tokens of currently active Users
    *  using constructor injection (Dependency injection)
    *  meaning this constructor is typically not used manually.
    */
    AttachmentsController(AttachmentsRepository attachmentsRepository, TokenRepository tokenRepository) {
        this.attachmentsRepository = attachmentsRepository;
        this.tokenRepository = tokenRepository;
    }

    /**
     * @param userId header that holds the requesting users id
     * @param token header that holds the requesting users auth token
     * @return all Users registered in the database
     * @throws NoAuthorizationException if user authentication fails
     * Lists all users saved in the database
     */
    @GetMapping("/attachments/{eventId}")
    List<AttachmentsInfoDTO> getInfoByEvent(@PathVariable Long eventId, @RequestHeader("userId") Long userId, @RequestHeader("token") String token) throws NoAuthorizationException{
        if(!tokenRepository.isValid(token, userId)){
            throw new NoAuthorizationException(userId);
        }
        List<AttachmentsEntity> attachments = (List<AttachmentsEntity>) attachmentsRepository.findAttachmentsByEventId(eventId);
        List<AttachmentsInfoDTO> infoDTOS = new ArrayList<>();
        for (AttachmentsEntity e: attachments) {
            infoDTOS.add(new AttachmentsInfoDTO(e.getId(), eventId, e.getName()));
        }
        return infoDTOS;
    }

    @PostMapping("/attachments/{eventId}")
    ResponseEntity<String> upload(@PathVariable Long eventId, @RequestParam("file")MultipartFile file){
        try{
            attachmentsRepository.save(new AttachmentsEntity(eventId, file.getOriginalFilename(), file.getBytes()));
        }
        catch (Exception e){
            throw new UploadFailedException();
        }
        return ResponseEntity.ok().body("Event: " + eventId +": File uploaded successfully");
    }

    @GetMapping("/attachments/{id}")
    ResponseEntity<ByteArrayResource> download(@PathVariable Long id){
        AttachmentsEntity e = attachmentsRepository.findById(id).orElseThrow(() -> new AttachmentNotFoundException(id));
        ByteArrayResource r = new ByteArrayResource(e.getAttachment());

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + e.getName() + "\"").body(r);
    }
}
