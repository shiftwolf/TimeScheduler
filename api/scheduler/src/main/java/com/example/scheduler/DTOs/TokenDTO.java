package com.example.scheduler.DTOs;

public class TokenDTO {
    private int userID;
    private String tokenString;

    public TokenDTO(int userID, String tokenString){
        this.userID = userID;
        this.tokenString = tokenString;
    }

    public int getUserID() { return userID;}

    public void setUserID(int userID) { this.userID = userID;}

    public String getTokenString() { return tokenString;}

    public void setTokenString(String tokenString) { this.tokenString = tokenString;}
}
