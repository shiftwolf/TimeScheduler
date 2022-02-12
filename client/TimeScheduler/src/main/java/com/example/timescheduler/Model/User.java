/**
 * @author Hendrik Weichel
 * @version 1.
 */

package com.example.timescheduler.Model;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.AttachmentsController;
import com.example.timescheduler.Controller.EventController;
import com.example.timescheduler.Controller.ScheduleController;
import com.example.timescheduler.Controller.UserController;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * This class describes the users. It provides functions to create, edit and delete event.
 * Users can have the attribute to be an admin. Admins can edit and view other profiles, also their
 * graphical user interface differs to the non-admin users.
 */
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

    // Constructors

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public User(Long id, String name, String email, String username) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Methods

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

    /**
     * Here a user, that is an admin can get all the Users that are stored in the database.
     * @param token Token of admin for verification.
     * @return List of Users.
     */
    public List<User> admin_getUsers(token token){
        try{
            return UserController.getUsers(token);
        }catch (InterruptedException | IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * With this method admins can edit a user object on the database.
     * @param token Token for verification.
     * @param id should be the id of the user that is about to get changed
     * @param email
     * @param username
     * @param name
     * @return Return message of Server.
     */
    public String admin_edit(token token, Long id, String email, String username, String name){
        if(!this.isAdmin()) {
            return null;
        }
        User newUser = new User(id, name, email,username);
        try{
            String message = UserController.changeUser(token, newUser);
            System.out.println(message);
            return message;
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * With this method a user is deleted from the database by an admin.
     * @param token Token to validate the admin
     * @param id Id of user that is going to be deleted.
     * @return return message.
     */
    public String admin_delete(token token, Long id){
        if(!this.isAdmin()) {
            return null;
        }
        try {
            return UserController.deleteUser(token, new User(id));
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * With this method users can get all their events.
     * @param token to validate user
     * @return List of events
     */
    public List<Event> getEvents(token token){
        try{
            return EventController.getEvents(token);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * With this method users can get a particular event's data by specifying the id.
     * @param token to validate the user
     * @param id to specify the event
     * @return the event with the given id
     */
    public Event getEventById(token token, Long id){
        try{
            return EventController.getEventById(token, id);
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * With this method a user can add/create a new event
     * @param name of event
     * @param date of event
     * @param duration of event
     * @param location of event
     * @param priority of event
     * @param participantMails of event
     * @param reminder of event
     * @param token to validate user
     * @return return message
     */
    public String addEvent(String name, Date date, Date duration, String location, Event.priorities priority, String[] participantMails, Date reminder, token token){

        User[] participants = new User[participantMails.length];
        for(int i = 0; i < participantMails.length; i ++){
            try {
                participants[i] = UserController.getUserByEmail(token, participantMails[i]);
            } catch (IOException | InterruptedException e){
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }

        Event event = new Event(name, date, duration,location,priority,participants, reminder);

        try{
            return EventController.newEvent(token, event, this);
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * With this a user can edit an existing event. It is required that the event already exists.
     * If you want to change only some properties of the event, just overwrite the others with the old values.
     * @param id of existing event that is changed
     * @param name that the event will have after execution of this function
     * @param date that the event will have after execution of this function
     * @param duration that the event will have after execution of this function
     * @param location that the event will have after execution of this function
     * @param priority that the event will have after execution of this function
     * @param participantMails that the event will have after execution of this function
     * @param reminder that the event will have after execution of this function
     * @param token to validate the user
     * @return return message
     */
    public String editEvent(Long id, String name, Date date, Date duration, String location, Event.priorities priority, String[] participantMails, Date reminder, token token) {

        User[] participants = new User[participantMails.length];
        for(int i = 0; i < participantMails.length; i ++){
            try {
                participants[i] = UserController.getUserByEmail(token, participantMails[i]);
            } catch (IOException | InterruptedException e){
                System.out.println(e.getMessage());
                return e.getMessage();
            }
        }

        Event event = new Event(id, name, date, duration,location,priority,participants, reminder);

        try{
            return EventController.changeEvent(token, event);
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Deletes Event out of users events.
     * Validation of Events existence not required, because delete button is displayed for every Event in GUI.
     * @pararm token to validate the user
     * @param id to specify the event
     * @return return message
     */
    public String deleteEvent(token token, Long id){
        try{
            return EventController.deleteEvent(token, id);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * Get the weekly schedule to later print out
     * @param token to validate the user
     * @return byte array of file
     */
    public byte[] getWeeklySchedule(token token){
        try {
            return ScheduleController.getWeeklySchedule(token);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String addParticipant(token token, String email, Long eventId){
        User user;
        try {
            user = UserController.getUserByEmail(token, email);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
        Event event = new Event(eventId);
        try{
            return EventController.addParticipantsViaEmail(token, user, event);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }

    // Todo: filename automatisch aus path erzeugen.
    public String uploadAtt(token token, Long id, String path, String filename){
        try {
            return AttachmentsController.uploadAtt(token, id, path, filename);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}