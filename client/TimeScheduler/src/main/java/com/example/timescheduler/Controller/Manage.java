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
        User user = new User("Mannfred", "mannii@1.de", "mannii1","av1234");
        user.setId(Long.valueOf(22));
        //newUser(user);
        token token = login(user.getUsername(), user.getPassword());

        User[] lsit = {};

        Event e3 = new Event("meeting", new Date(1234132), new Date(1234133), "ffm", Event.priorities.RED,lsit);

        Event e4 = new Event("frühstücken", new Date(1234132), new Date(1234133), "ffm", Event.priorities.RED,lsit);

        e4.setId(Long.valueOf(28));

        //changeEvent(token, e4);

        addParticipantsViaEmail(token, user, e4);

        logout(token);
        //Event e1 = getEventById(token, user.getId());
        //System.out.println(e1.toString());
        //System.out.println(e1.getName());

        //List<Event> evList1 = getEvents(token);

       // for(Event event : evList1){
         //   System.out.println(event.toString());
            //for(User user: event.getParticipantsEntities())
            //System.out.println(event.getParticipantsEntities().get(0).getClass().getName());
        //}

        //Event e1 = getEvents(token);
        //System.out.println(e1.toString());

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
                .uri(URI.create(url + "/users/id=" + String.valueOf(id)))
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
                .uri(URI.create(url + "/users/name=" + String.valueOf(username)))
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
                .uri(URI.create(url + "/users/email=" + String.valueOf(email)))
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

        System.out.println(response.body());

        // mapper json -> pojo java
        ObjectMapper mapper = new ObjectMapper();

        // type reference to declare the type of return value
        List<Event> events = mapper.readValue(response.body(), new TypeReference<List<Event>>(){});

        return events;

    }

    public static Event getEventById(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/events/id=" + String.valueOf(id)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();

        Event event = mapper.readValue(response.body(), Event.class);

        return event;
    }

    public static String newEvent(token token, Event event, User user) throws IOException, InterruptedException {
        //user -> json
        ObjectMapper mapper = new ObjectMapper();
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonUser = mapper.writeValueAsString(event);
        System.out.println(jsonUser);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .uri(URI.create(url + "/events"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

    public static void deleteEvent(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .uri(URI.create(url + "/events/id=" + String.valueOf(id)))
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
                .uri(URI.create(url + "/events/id=" + String.valueOf(event.getId())))
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

    public static String addParticipantsViaEmail(token token, User user, Event event) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(""))
                .uri(URI.create(url + "/events/id=" + String.valueOf(event.getId()) + "/participants/email=" + String.valueOf(user.getEmail())))
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
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