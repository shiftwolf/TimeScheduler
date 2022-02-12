package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;

import java.io.IOException;
import java.util.List;

public interface HomeViewListener {

    void setUser(User loggedUser);

    User getLoggedUser(token token);

    List <Event> getEvents(token token) throws IOException, InterruptedException;

    void deleteEvent(token token, Event event);

    // admin only:

    List<User> admin_getUsers(token token);

    void admin_editUser(token token, User user, String newUsername, String newName, String newEmail);

    void admin_deleteUser(token token, User user);

}
