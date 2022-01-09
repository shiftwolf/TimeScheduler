package com.example.scheduler.entities;

import org.springframework.data.annotation.PersistenceConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "scheduler")
public class UsersEntity {

    @Id
    @Column(name = "id")
    private int id;
    private Timestamp createdAt;
    private String email;
    private String username;
    private String name;
    private String hashedpw;

    public UsersEntity(String email,
                String username,
                String name,
                String hashedpw) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.hashedpw = hashedpw;
    }

    public UsersEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "hashedpw")
    public String getHashedpw() {
        return hashedpw;
    }

    public void setHashedpw(String hashedpw) {
        this.hashedpw = hashedpw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id && Objects.equals(createdAt, that.createdAt) && Objects.equals(email, that.email) && Objects.equals(username, that.username) && Objects.equals(name, that.name) && Objects.equals(hashedpw, that.hashedpw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, email, username, name, hashedpw);
    }
}
