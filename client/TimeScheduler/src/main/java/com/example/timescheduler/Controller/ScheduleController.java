package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.DeSerializer.ParticipantDeserializer;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Collectors;

public class ScheduleController {
    static String url = Manage.url;

    public static byte[] getWeeklySchedule(token token) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userId", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/pdf/generate"))
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        System.out.println(response.body());

        InputStream dataStream = response.body();
        return dataStream.readAllBytes();

//        try (FileOutputStream stream = new FileOutputStream("/Users/wolf/Documents/test/third.pdf")) {
//            stream.write(bytes);
//        }


    }

}
