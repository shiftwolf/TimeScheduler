package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when trying to register a new user and a find on a {@link com.example.scheduler.entities.UsersEntity} with a given name returns not null
 */
public class UsernameTakenException extends RuntimeException{

    public UsernameTakenException(String username){
        super(username + " is already taken");
    }
}
