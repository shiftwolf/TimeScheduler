package com.example.scheduler.DTOs;

import java.util.Objects;

public class UserDTO {

    private Long id;
    private String username;
    private String name;
    private String email;
    private boolean isAdmin;

    public UserDTO()  {}

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
