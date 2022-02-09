package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.DeSerializer.ParticipantDeserializer;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


/**
 * @author Hendrik Weichel
 * @version 2.0
 * holds all methods for requests about events to the server
 */
public class EventController {

    /**
     * Holds the url of the server, that is initialized in the com.example.timescheduler.Controller.Manage class.
     */
    static String url = Manage.url;

    /**
     * A User can get all its events.
     * @param token Token that validates the user.
     * @return A List of Model.Event objects
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
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

        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new ParticipantDeserializer());
        mapper.registerModule(module);

        // type reference to declare the type of return value
        List<Event> events = mapper.readValue(response.body(), new TypeReference<List<Event>>() {
        });

        return events;

    }

    /**
     * A user can get an Events data that belongs to it, with using just the Id.
     * @param token Token that validates the user
     * @param id Id of the searched Event
     * @return A Model.Event object
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
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

        SimpleModule module = new SimpleModule();
        module.addDeserializer(User.class, new ParticipantDeserializer());
        mapper.registerModule(module);

        Event event = mapper.readValue(response.body(), Event.class);

        return event;
    }

    /**
     * User can create a new Event in the database in the server.
     * @param token Token that validates the user
     * @param event Model.Event object that should be inserted in the database
     * @param user User that creates the event, matches to the token
     * @return Return message given by the server
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
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

    /**
     * User can delete an Event he belongs to
     * @param token Token that validates the user
     * @param id Id of the event that should be deleted
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
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

    /**
     * User can change an events data
     * @param token Token that validates the user
     * @param event Model.Event object that represents the new event
     * @return Return message of the server
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
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

    /**
     * User can add a participant to an event via its email
     * @param token Token that validates the user
     * @param user Model.User object that should be added to the event
     * @param event Event to which the new participant should be added
     * @return Return message of Server
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
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

}
