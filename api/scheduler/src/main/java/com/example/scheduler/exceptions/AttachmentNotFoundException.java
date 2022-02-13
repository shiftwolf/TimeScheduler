package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when a find on an {@link com.example.scheduler.entities.AttachmentsEntity} returns null or an error
 */
public class AttachmentNotFoundException extends RuntimeException{
    public AttachmentNotFoundException(Long id) {
        super("Could not find file " +id);
    }
}
