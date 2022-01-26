package com.example.scheduler.entities;

import javax.persistence.*;

@Entity
@Table(name = "tokens", schema = "scheduler")
public class TokensEntity {
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private Integer userId;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
