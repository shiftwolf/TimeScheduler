package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.AttachmentsInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Hendrik Weichel
 * @version 2.0
 * holds all methods for requests about the attachments of an event to the server
 */
public class AttachmentsController {

    /**
     * Holds the url of the server, that is initialized in the com.example.timescheduler.Controller.Manage class.
     */
    static String url = Manage.url;

    /**
     *
     * @param token
     * @param id
     * @return
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static List<AttachmentsInfo> getInfoByEvent(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userID", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/attachments/eventId=" + String.valueOf(id)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        ObjectMapper mapper = new ObjectMapper();

        List<AttachmentsInfo> at = mapper.readValue(response.body(), new TypeReference<List<AttachmentsInfo>>(){});

        return at;

    }

    /**
     *
     * @param token
     * @param id
     * @return
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static byte[] downloadAtt(token token, Long id) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("token", token.getTokenString())
                .header("userId", String.valueOf(token.getUserID()))
                .uri(URI.create(url + "/attachments/id=" + id))
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        InputStream dataStream = response.body();
        System.out.println(response.body());

        byte[] bytes = dataStream.readAllBytes();
        return bytes;

//        try (FileOutputStream stream = new FileOutputStream("/Users/wolf/Downloads/Bonusprogramm.pdf")) {
//              stream.write(bytes);
//        }
    }

    /**
     *
     * @param token
     * @return
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String uploadAtt(token token, Long eventId, String path, String fileName)
            throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("token", token.getTokenString())
                .header("userId", String.valueOf(token.getUserID()))
                .header("fileName", fileName)
                .uri(URI.create(url + "/attachments/eventId=" + eventId))
                .POST(HttpRequest.BodyPublishers.ofFile(Paths.get(path)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String removeAtt(token token, Long attId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(url + "/attachments/id=" + String.valueOf(attId)))
                .header("userId", String.valueOf(token.getUserID()))
                .header("token", token.getTokenString())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        return response.body();
    }

}
