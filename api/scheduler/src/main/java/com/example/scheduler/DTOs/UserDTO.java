package com.example.scheduler.DTOs;

import java.util.Objects;

public class UserDTO {

    private int id;
    private String username;
    private String name;
    private String email;

    public UserDTO()  {}

    public UserDTO(int id, String username, String name, String email){
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getUsername() { return username;}

    public void setUsername(String username) { this.username = username;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getEmail() { return email;}

    public void setEmail(String email) { this.email = email;}
}
