/**
 * @author Hendrik Weichel
 * @version 1.
 */

package com.example.timescheduler.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    // Attributes
    public Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private Date createdAt;
    private List<Event> events;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", events=" + events +
                '}';
    }

    // Constructors

    public User() {
    }
    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(Long id, String email, String username, String name, String password, List<Event> events) {
        this.id = id;
        this.createdAt = new Date();;
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.events = events;
    }


    // Getters

    public String getPassword() {
        return password;
    }

    public Long getId() {return id;}
    public Date getCreated_at() {return createdAt;}

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

    public void setId(Long id) {
        this.id = id;
    }

    // Methods
    public void addEvent(Event event){}

    public boolean createEvent(String name, Date date, Date duration, String location, String description, Event.priorities priority, List<User> participants){
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
