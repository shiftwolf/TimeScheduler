package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Date;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        User user = new User("Mannfred", "manni@1.de", "manni1","av1234");

        user.setId(Long.valueOf(4));

        List<User> list = new ArrayList<User>();
        Date d = new Date();
        Date dur = new Date();

        Event testevent = new Event("meeting",d, dur,"ffm",Event.priorities.RED,  list);

        //getUsers(t1);

        token token = login(user.getUsername(), user.getPassword());

        System.out.println(token.toString());

        deleteUser(token, user);

        //User user = getUserById(t1, Long.valueOf(13));
        //System.out.println(user.getName());

        //List<Event> events = getEvents(t1);
        //for(Event event: events){System.out.println(event.toString());for(User user: event.getParticipantsEntities()){System.out.println(user.toString());}}

        //Event event = getEventById(t1, Long.valueOf(1));
        //System.out.println(event.toString());

        //String response = newUser(user);
        //System.out.println(response);

        //String response = newEvent(t1, testevent, user);

        //token token = login(user);
        //System.out.println(token.getTokenString() + token.getUserID());

        //logout(t1);

        //Event e1 = new Event();
        //e1.setId(2);
        //deleteEvent(t1, e1);
    }



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
        List<User> users = mapper.readValue(response.body(), new TypeReference<List<User>>() {});

        return users;
    }

    public static User getUserById(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users?id=" + String.valueOf(id)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(response.body(), new TypeReference<User>(){});

        return user;
    }

    public static User getUserByUsername(token token, String username) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users?username=" + String.valueOf(username)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(response.body(), new TypeReference<User>(){});

        return user;
    }

    public static User getUserByEmail(token token, String email) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/users?email=" + String.valueOf(email)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        User user = mapper.readValue(response.body(), new TypeReference<User>(){});

        return user;
    }

    public static String newUser(User user) throws IOException, InterruptedException {
        //user -> json
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(user);

        jsonUser = jsonUser.substring(0,jsonUser.length()-1) + ",\"password\":\""+ user.getPassword() + "\"}";

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

    public static String changeUser(token token, User user) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(user);

        jsonUser = jsonUser.substring(0,jsonUser.length()-1) + ",\"password\":\""+ user.getPassword() + "\"}";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonUser))
                .uri(URI.create(url + "/users/" + String.valueOf(user.getId())))
                .header("userID", String.valueOf(token.getUserID()))
                .header("tokenString", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

    public static void deleteUser(token token, User user) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/users/id=" + user.getId()))
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

    }


    public static List<Event> getEvents(token token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/events"))
                .build();

        // bodyhandler sets what is responded to response.body
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // mapper json -> pojo java
        ObjectMapper mapper = new ObjectMapper();

        // type reference to declare the type of return value
        List<Event> events = mapper.readValue(response.body(), new TypeReference<List<Event>>() {});

        // TODO: map events.user into java

        return events;

    }

    public static Event getEventById(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/events/" + String.valueOf(id)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();

        Event event = mapper.readValue(response.body(), new TypeReference<Event>(){});

        return event;
    }

    public static String newEvent(token token, Event event, User user) throws IOException, InterruptedException {
        //user -> json
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(event);

        System.out.println(jsonUser);

        HttpClient client = HttpClient.newHttpClient();

        // TODO: richtig formattieren

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url + "/users"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public static void deleteEvent(token token, Event event) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("userID", String.valueOf(token.getUserID()))
                .header("tokenString", token.getTokenString())
                .uri(URI.create(url + "/events/" + String.valueOf(event.getId())))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }

    public static String changeEvent(token token, Event event) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(event);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonUser))
                .uri(URI.create(url + "/users/" + String.valueOf(event.getId())))
                .header("userID", String.valueOf(token.getUserID()))
                .header("tokenString", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }


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

        token token = mapper.readValue(response.body(), new TypeReference<token>(){} );

        System.out.println(response.body());

        return token;

    }

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