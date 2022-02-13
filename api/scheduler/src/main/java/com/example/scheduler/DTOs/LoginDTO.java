package com.example.scheduler.DTOs;
/**
 * @author Max
 * @version 1.0
 * Data Transfer Object used to map Request Body JSON to, holds users login data
 */
public class LoginDTO {
    private String username;
    private String password;

    /**
     * normal constructor used to initialize the values
     * @param username of the user
     * @param password plaintext password of the user
     */
    public LoginDTO(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password;}

    public void setPassword(String password) { this.password = password;}
}
