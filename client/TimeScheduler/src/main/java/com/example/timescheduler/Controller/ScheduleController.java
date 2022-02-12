package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

        InputStream dataStream = response.body();
        return dataStream.readAllBytes();

//        try (FileOutputStream stream = new FileOutputStream("/Users/wolf/Documents/test/third.pdf")) {
//            stream.write(bytes);
//        }


    }

}
