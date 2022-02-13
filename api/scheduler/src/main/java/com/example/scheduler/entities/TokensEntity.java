package com.example.scheduler.entities;

import javax.persistence.*;
import java.util.UUID;

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
