package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when a find on an {@link com.example.scheduler.entities.UsersEntity} returns null or an error
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Could not find user with id: " + id);
    }

    public UserNotFoundException(String name) {
        super("Could not find user with: " + name);
    }
}
