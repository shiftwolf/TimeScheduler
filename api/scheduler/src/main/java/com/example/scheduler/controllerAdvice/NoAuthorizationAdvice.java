package com.example.scheduler.controllerAdvice;

import com.example.scheduler.exceptions.NoAuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Max
 * @version 1.0
 * Controller Advice that holds the Handler for the {@link com.example.scheduler.exceptions.NoAuthorizationException}
 */
@ControllerAdvice
public class NoAuthorizationAdvice {
    /**
     * If the Exception class gets thrown during runtime this method is executed
     * @param ex NoAuthorizationException the Handler listens to
     * @return the Exception Message as a String to the HTTP Response Body
     */
    @ResponseBody
    @ExceptionHandler(NoAuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    String noAuthorizationHandler(NoAuthorizationException ex){
        return ex.getMessage();
    }
}
