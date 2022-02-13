package com.example.timescheduler.Model;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.LoginController;
import com.example.timescheduler.Controller.UserController;

import java.io.IOException;

/**
 * This class contains methods that need to be invoked without a user object.
 */
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
     * @return 0 - user created successfully
     * @return 1 - email is already taken
     * @return 2 - username is already taken
     * @return 3 - server error
     */
    public static int createUser(String name, String email, String username, String password){
        User newUser = new User(name, email ,username, password);
        try{
            String message = UserController.newUser(newUser);
            if (message.endsWith("created successfully")){
                return 0;
            } else if (message.startsWith("There already exists an account registered with this email:")){
                return 1;
            } else if (message.endsWith("is already taken")){
                return 2;
            } else {
                return 3;
            }

        } catch (IOException | InterruptedException e){
            System.out.println(e.getMessage());
            return 3;
        }

    }
}