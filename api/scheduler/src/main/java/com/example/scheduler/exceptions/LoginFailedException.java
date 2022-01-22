package com.example.scheduler.exceptions;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
        super("Login failed, wrong Username oder Password");
    }
}
