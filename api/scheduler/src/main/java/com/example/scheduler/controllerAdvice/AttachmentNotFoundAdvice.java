package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.AttachmentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Max
 * @version 1.0
 * Controller Advice that holds the Handler for the {@link com.example.scheduler.exceptions.AttachmentNotFoundException}
 */
@ControllerAdvice
public class AttachmentNotFoundAdvice {

    /**
     * If the Exception class gets thrown during runtime this method is executed
     * @param ex AttachmentNotFoundException the Handler listens to
     * @return the Exception Message as a String to the HTTP Response Body
     */
    @ResponseBody
    @ExceptionHandler(AttachmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String attachmentNotFoundHandler(AttachmentNotFoundException ex){ return ex.getMessage();}
}
