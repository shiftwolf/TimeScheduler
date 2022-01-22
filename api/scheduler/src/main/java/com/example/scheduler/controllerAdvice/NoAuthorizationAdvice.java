package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.NoAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoAuthorizationAdvice {

    @ResponseBody
    @ExceptionHandler(NoAuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String noAuthorizationHandler(NoAuthorizationException ex){
        return ex.getMessage();
    }
}
