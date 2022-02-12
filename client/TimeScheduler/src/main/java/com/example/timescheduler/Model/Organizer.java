package com.example.timescheduler.Model;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.LoginController;
import com.example.timescheduler.Controller.UserController;

import java.io.IOException;

public class Organizer {

    /**
     * Here we get the user's data by input of the token.
     * @return the POJO User object with data of database
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

    /**
     * Function to log in and get token that validates user in the current session.
     * @param username of user account
     * @param password of user account
     * @return token to validate user in session
     */
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

    /**
     * This deletes the token out of the database.
     * @param token to delete
     * @return return message of request
     */
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
     * @param name of new user
     * @param email of new user
     * @param username of new user
     * @param password of new user
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