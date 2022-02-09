package com.example.timescheduler.Model;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.LoginController;
import com.example.timescheduler.Controller.UserController;

import java.io.IOException;

public class Organizer {

    /**
     * Here we get the user's data by input of the token.
     * @return
     */
    public static User getUserByToken(token token){
        User user;
        try{
            user = UserController.getUserById(token, token.getUserID());
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }
        return user;
    }

    public static token login(String username, String password) throws IOException, InterruptedException {
        token token;
        try{
            token = LoginController.login(username, password);
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }

        return token;
    }

    public static String logout(token token){
        try{
            return LoginController.logout(token);
        }catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
        return null;
        }
    }
}

