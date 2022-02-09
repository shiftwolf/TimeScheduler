/**
 * @author Hendrik Weichel
 * @version 1.
 */

package com.example.timescheduler.Model;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.UserController;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.IOException;
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
    private boolean admin;

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
                ", admin=" + admin +
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
        this.createdAt = new Date();
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.events = events;
    }

    // Getters

    public boolean isAdmin() {
        return admin;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {return id;}

    public Date getCreatedAt() {return createdAt;}

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

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setAdmin(boolean admin){
        this.admin = admin;
    }

    // Methods

    /**
     * Here an admin, that is a User can get all the Users that are stored in the database.
     * @param token Token of admin for verification.
     * @return List of Users.
     */
    public List<User> admin_getUsers(token token){
        if(!this.isAdmin()){
            return null;
        }
        try{
            return UserController.getUsers(token);
        }catch (InterruptedException | IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String edit(token token, Long id, String email, String username, String name, String password, List<Event> events){
        User temp = new User(id, email, username, name, password, events);
        try{
            String message = UserController.changeUser(token, temp);
            System.out.println(message);
            return message;
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    public String delete(token token){
        try {
            String message = UserController.deleteUser(token, this);
            return message;
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     *
     * @param event
     */
    public void addEvent(Event event){}

    /**
     *
     * @param name
     * @param date
     * @param duration
     * @param location
     * @param description
     * @param priority
     * @param participants
     * @return
     */
    public boolean createEvent(String name, Date date, Date duration, String location, String description, Event.priorities priority, List<User> participants){
        return true;
    }

    /**
     * Deletes Event out of users events.
     * Validation of Events existance not required, because delete button is displayed for every Event in GUI.
     * @param event
     */
    public void deleteEvent(Event event){
        return;
    }

}
