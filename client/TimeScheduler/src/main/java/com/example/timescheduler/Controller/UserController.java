package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * @author Hendrik Weichel
 * @version 2.0
 * Holds all methods for requests about login to the server.
 */
public class UserController {
    /**
     * Holds the url of the server, that is initialized in the com.example.timescheduler.Controller.Manage class.
     */
    static String url = Manage.url;

    /**
     * Here an admin can get all the Users of the database.
     * @param token Admin has to be logged in to be validated
     * @return List of all Users of the database
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static List<User> getUsers(token token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users"))
                .build();

        // bodyhandler sets what is responded to response.body
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        // mapper json -> pojo java
        ObjectMapper mapper = new ObjectMapper();

        // type reference to declare the type of return value
        List<User> users = mapper.readValue(response.body(), new TypeReference<List<User>>() {
        });

        return users;
    }

    /**
     * Here a user can get his/her data with just the id.
     * @param token User has to be logged in to be validated
     * @param id Id of User
     * @return Returns a Model.User Object.
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static User getUserById(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users/id=" + String.valueOf(id)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(response.body(), new TypeReference<User>() {
        });

        return user;
    }

    /**
     * Here a user can get its account data with just the username.
     * @param token User has to be logged in to be validated
     * @param username Username of User
     * @return Returns a Model.User Object.
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static User getUserByUsername(token token, String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users/name=" + String.valueOf(username)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(response.body(), new TypeReference<User>() {
        });

        return user;
    }

    /**
     * Here a user can get its data with just the email.
     * @param token User has to be logged in to be validated
     * @param email Email address of User
     * @return Returns a Model.User Object.
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static User getUserByEmail(token token, String email) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users/email=" + String.valueOf(email)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(response.body(), new TypeReference<User>() {
        });

        return user;
    }

    /**
     * Here a new User is created on the database.
     * @param user User to be created
     * @return A String with the return message. Says whether the User could be created or not.
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String newUser(User user) throws IOException, InterruptedException {
        //user -> json
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(user);

        System.out.println(jsonUser);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url + "/users"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

    /**
     * Here a User can change its account data. By passing a new User object with the new account data.
     * @param token User has to be logged in to be validated
     * @param user New account data
     * @return A String with the return message. Says whether the User could be changed or not.
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String changeUser(token token, User user) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(user);

        jsonUser = jsonUser.substring(0, jsonUser.length() - 1) + ",\"password\":\"" + user.getPassword() + "\"}";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonUser))
                .uri(URI.create(url + "/users/id=" + String.valueOf(user.getId())))
                .header("userID", String.valueOf(token.getUserID()))
                .header("tokenString", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

    /**
     * Deletes User in the Database.
     * @param token User has to be logged in to be validated
     * @param user User Object that should be deleted in the database
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String deleteUser(token token, User user) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/users/id=" + user.getId()))
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();

    }


}
