package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmailAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String emailAlreadyTakenHandler(EmailAlreadyExistsException ex){
        return ex.getMessage();
    }
}
