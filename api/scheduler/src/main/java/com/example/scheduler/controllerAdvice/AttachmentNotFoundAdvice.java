package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.AttachmentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AttachmentNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(AttachmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String attachmentNotFoundHandler(AttachmentNotFoundException ex){ return ex.getMessage();}
}
