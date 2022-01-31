/**
 * @author Hendrik Weichel
 * @version 1.
 */

package com.example.timescheduler.Model;

import java.util.Date;
import java.util.List;

public class User {
    // Attributes
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;

    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    private Date created_at;
    private List<Event> events;

    // Constructor
    public User(Long id, String email, String username, String name, String password, List<Event> events) {
        this.id = id;
        this.created_at = new Date();;
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.events = events;
    }

    // Getters
    public Long getId() {return id;}

    public Date getCreated_at() {return created_at;}

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {return events;}

    // Methods
    public void addEvent(Event event){events.add(event);}

    public boolean createEvent(String name, Date date, Date duration, String location, String description, Event.priorities priority, ArrayList<User> participants){
        if(date == null){
            return false;
        }
        if(duration == null){
            return false;
        }
        // TODO : upload Event to DB
        // TODO : get id from DB
        for(User cursor : participants){
            //  cursor.addEvent(tempEvent);
        }
        // TODO : getID form DB and put into model

        return true;
    }

    /**
     * Deletes Event out of users events.
     * Validation of Events existance not required, because delete button is displayed for every Event in GUI.
     * @param event
     */
    public void deleteEvent(Event event){
    }

}
