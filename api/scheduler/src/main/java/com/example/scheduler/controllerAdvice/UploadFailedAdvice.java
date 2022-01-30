package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.UploadFailedException;
import com.example.scheduler.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UploadFailedAdvice {

    @ResponseBody
    @ExceptionHandler(UploadFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String uploadFailedHandler(UploadFailedException ex){ return ex.getMessage();}
}
