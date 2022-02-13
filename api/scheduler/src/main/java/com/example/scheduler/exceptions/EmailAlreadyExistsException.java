package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when trying to register a new user and a find on a {@link com.example.scheduler.entities.UsersEntity} with a given email returns not null
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email){
        super("There already exists an account registered with this email: " + email);
    }
}
