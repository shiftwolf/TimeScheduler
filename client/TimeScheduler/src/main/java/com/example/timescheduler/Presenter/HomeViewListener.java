package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;

import java.io.IOException;
import java.util.List;

public interface HomeViewListener {

    List <Event> getEvents(token token) throws IOException, InterruptedException;

    List<User> getUsers(token token) throws IOException, InterruptedException;

    void editUser(token token, User user, String newUsername, String newName, String newEmail);

}
