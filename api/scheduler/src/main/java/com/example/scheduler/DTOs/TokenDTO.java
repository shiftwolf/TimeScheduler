package com.example.scheduler.DTOs;

public class TokenDTO {
    private Long userID;
    private String tokenString;

    public TokenDTO(Long userID, String tokenString){
        this.userID = userID;
        this.tokenString = tokenString;
    }

    public Long getUserID() { return userID;}

    public void setUserID(Long userID) { this.userID = userID;}

    public String getTokenString() { return tokenString;}

    public void setTokenString(String tokenString) { this.tokenString = tokenString;}
}
