package com.example.scheduler.DTOs;
/**
 * @author Max
 * @version 1.0
 * Data Transfer Object used to map Request Body JSON to, holds data to generate a new {@link com.example.scheduler.entities.UsersEntity}
 */
public class NewUserDTO {

    private String username;
    private String name;
    private String email;
    private String password;

    NewUserDTO()  {}
    /**
     * normal constructor used to initialize the values
     * @param username pseudonym the user wants to be identified by (also used for the login)
     * @param name real name of the user
     * @param email of the user, (the address used to send the user reminders)
     * @param password the user wants to use as his login password
     */
    public NewUserDTO(String username, String name, String email, String password){
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password;}
}
