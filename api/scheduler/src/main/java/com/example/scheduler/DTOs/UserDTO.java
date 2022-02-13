package com.example.scheduler.DTOs;

/**
 * @author Max
 * @version 1.0
 * Data Transfer Object used to generate Response Body to hold Info about an {@link com.example.scheduler.entities.UsersEntity}
 */
public class UserDTO {

    private Long id;
    private String username;
    private String name;
    private String email;
    private boolean isAdmin;

    public UserDTO()  {}

    /**
     * normal constructor used to initialize the values
     * @param username pseudonym the user wants to be identified by (also used for the login)
     * @param name real name of the user
     * @param email of the user, (the address used to send the user reminders)
     * @param isAdmin <code>true</code> if the user has admin privileges, else <code>false</code>
     */
    public UserDTO(Long id, String username, String name, String email, boolean isAdmin){
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public Long getId() { return id;}

    public void setId(Long id) { this.id = id;}

    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}

    public boolean isAdmin() { return isAdmin;}

    public void setAdmin(boolean admin) { isAdmin = admin;}
}
