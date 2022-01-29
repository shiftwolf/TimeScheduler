package com.example.timescheduler.APIobjects;

public class token {

    private Long userID;

    private String tokenString;

    public token() {};

    public token(Long userID, String tokenString) {
        this.userID = userID;
        this.tokenString = tokenString;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

    public Long getUserID() {
        return userID;
    }

    public String getTokenString() {
        return tokenString;
    }

}
