package com.example.scheduler.exceptions;

public class AttachmentNotFoundException extends RuntimeException{
    public AttachmentNotFoundException(Long id) {
        super("Could not find file " +id);
    }
}
