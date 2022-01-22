package com.example.scheduler.exceptions;

public class NoAuthorizationException extends RuntimeException {

    public NoAuthorizationException(Long id){
        super("user " + id + " has no authorization for this request");
    }
}
