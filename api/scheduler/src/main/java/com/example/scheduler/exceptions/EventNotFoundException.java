package com.example.scheduler.exceptions;

/**
 * @author Max
 * @version 1.0
 * Exception that is thrown when a find on an {@link com.example.scheduler.entities.EventsEntity} returns null or an error
 */
public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(Long id) {
        super("Could not find event " + "id");
    }
}
