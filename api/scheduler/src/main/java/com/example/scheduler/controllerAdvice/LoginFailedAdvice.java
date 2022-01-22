package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.LoginFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LoginFailedAdvice {
    @ResponseBody
    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String loginFailedHandler(LoginFailedException ex){
        return ex.getMessage();
    }
}
