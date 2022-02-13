package com.example.scheduler.entities;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Timo, Max
 * @version 1.0
 * JPA Repository Entity, this is the java representation of a sql table
 * Stores token data of logged-in users, to validate their requests
 */
@Entity
@Table(name = "tokens", schema = "scheduler")
public class TokensEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private Long userId;

    /**
     * normal constructor used to initialize the values, creates a unique token using {@link java.util.UUID#randomUUID()}
     * @param userId database identifier of the user logging in
     */
    public TokensEntity(Long userId){
       this.token = UUID.randomUUID().toString();
       this.userId = userId;
    }
    public TokensEntity(){

    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) { this.token = token;}

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
