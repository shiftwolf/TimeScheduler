package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when a request is performed with a faulty token or the user has no authorization
 * */
public class NoAuthorizationException extends RuntimeException {

    public NoAuthorizationException(Long id){
        super("user " + id + " has no authorization for this request");
    }
}
