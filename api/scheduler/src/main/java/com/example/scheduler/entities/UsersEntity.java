package com.example.scheduler.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Timo, Max
 * @version 1.0
 * JPA Repository Entity, this is the java representation of a sql table
 * Stores User data
 */
@Entity
@Table(name = "users", schema = "scheduler")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Basic
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "username")
    private String username;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "hashedpw")
    private String hashedpw;

    @Basic
    @Column(name = "is_admin")
    private boolean isAdmin;

    /**
     * normal constructor used to initialize the values
     * @param email of the user, (the address used to send the user reminders)
     * @param username pseudonym the user wants to be identified by (also used for the login)
     * @param name real name of the user
     * @param hashedpw the user wants to use as his login password
     * @param isAdmin set if the user has admin privileges
     */
    public UsersEntity(String email,
                String username,
                String name,
                String hashedpw,
                boolean isAdmin) {
        this.email = email;
        this.username = username;
        this.name = name;
        this.hashedpw = hashedpw;
        this.isAdmin = isAdmin;
    }

    public UsersEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getHashedpw() {
        return hashedpw;
    }

    public boolean isAdmin() { return isAdmin;}

    public void setAdmin(boolean admin) { isAdmin = admin;}

    /**
     * hashes the password of the user using {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}
     */
    public UsersEntity hashPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());
        this.hashedpw = encoder.encode(this.hashedpw);
        return this;
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
