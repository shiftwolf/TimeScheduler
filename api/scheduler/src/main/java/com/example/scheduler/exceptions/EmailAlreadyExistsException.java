package com.example.scheduler.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email){
        super("There already exists an account registered with this email: " + email);
    }
}
