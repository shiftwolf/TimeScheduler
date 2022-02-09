package com.example.timescheduler.Controller;

import com.example.timescheduler.APIobjects.token;

import java.io.IOException;

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
    public static String getInfoByEvent(token token, Long id){
        return " ";

    }

    /**
     *
     * @param token
     * @param id
     * @return
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String downloadAtt(token token, Long id){
        return "";
    }

    /**
     *
     * @param token
     * @return
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    public static String uploadAtt(token token){
        return "";
    }

}
