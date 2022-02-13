package com.example.scheduler.DTOs;
/**
 * @author Max
 * @version 1.0
 * Data Transfer Object used to generate Response Body to hold login token {@link com.example.scheduler.entities.TokensEntity}
 */
public class TokenDTO {
    private Long userID;
    private String tokenString;

    /**
     * normal constructor used to initialize the values
     * @param userID database identifier of the {@link com.example.scheduler.entities.UsersEntity}
     * @param tokenString unique string used to validate the clients requests
     */
    public TokenDTO(Long userID, String tokenString){
        this.userID = userID;
        this.tokenString = tokenString;
    }

    public Long getUserID() { return userID;}

    public void setUserID(Long userID) { this.userID = userID;}

    public String getTokenString() { return tokenString;}

    public void setTokenString(String tokenString) { this.tokenString = tokenString;}
}
