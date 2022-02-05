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
 * @author Hendrik
 * @version 1.0
 *
 * Manage Class contains basic functions of communicating with server.
 *
 */

public class Manage {

    // --- insert your the target IP here
    //static String url = "http://192.168.178.172:8090";
    static String url = "http://192.168.178.28:8090";

    public static void main(String[] args) {
        try {
            getUsers();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public token login(String username, String password) {

        // can be used to send request and receive http
        HttpClient client = HttpClient.newHttpClient();

        // request var
        //HttpRequest request = HttpRequest.newBuilder();

        return null;
    }

    public static void getUsers() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", "a2cc029c-7b38-41ba-98b3-86c287349f3c")
                .header("userID", "13")
                .uri(URI.create(url + "/users"))
                .build();

        // bodyhandler sets what is responded to response.body
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // mapper json -> pojo java
        ObjectMapper mapper = new ObjectMapper();

        // type reference to declare the type of return value
        List<User> users = mapper.readValue(response.body(), new TypeReference<List<User>>() {});

        users.forEach(System.out::println);
    }

}