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

    public static token login(String username, String password) {
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

    /**
     * Creates a new user in server and returns user
     * @param name
     * @param email
     * @param username
     * @param password
     * @return created user
     */
    public static User createUser(String name, String email, String username, String password){
        User newUser = new User(name, email ,username, password);
        try{
            UserController.newUser(newUser);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }
        try{
            newUser = UserController.getUserByUsername(login(username, password), username);
        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return null;
        }
        return newUser;
    }
}