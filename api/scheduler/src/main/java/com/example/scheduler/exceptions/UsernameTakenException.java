package com.example.scheduler.exceptions;

public class UsernameTakenException extends RuntimeException{

    public UsernameTakenException(String username){
        super(username + " is already taken");
    }
}
