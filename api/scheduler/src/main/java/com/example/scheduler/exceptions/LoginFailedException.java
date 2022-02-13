package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when {@link com.example.scheduler.DTOs.LoginDTO} data does not match up with the data of an {@link com.example.scheduler.entities.UsersEntity}
 */
public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
        super("Login failed, wrong Username oder Password");
    }
}
