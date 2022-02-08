package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Hendrik Weichel
 * @version 2.0
 * holds all methods for requests about login to the server
 */
public class LoginController {

    /**
     * Holds the url of the server, that is initialized in the com.example.timescheduler.Controller.Manage class.
     */
    static String url = Manage.url;

    /**
     * User logs in and gets the back the token to later use other controller functions
     * @param username Username to log in
     * @param password Password thats hash is compared with the hash in the database
     * @return Token that can be later used for ther controller functions
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static token login(String username, String password) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();

        String jsonUser = "{\"username\": \"" + username + "\",\"password\": \"" + password + "\"}";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        token token = mapper.readValue(response.body(), new TypeReference<token>() {
        });

        System.out.println(response.body());

        return token;

    }

    /**
     * User logs out and token is deleted in the database.
     * @param token Token hat user has received in the login
     * @return String with the return message
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String logout(token token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .uri(URI.create(url + "/login"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

}
