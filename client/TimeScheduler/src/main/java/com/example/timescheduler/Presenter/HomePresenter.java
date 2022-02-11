package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.EventController;
import com.example.timescheduler.Controller.UserController;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.Organizer;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;

import java.io.IOException;
import java.util.List;

public class HomePresenter implements HomeViewListener {

    private final HomeView homeView;
    private final User user;

    public HomePresenter(HomeView homeView, User user) {
        this.homeView = homeView;
        this.homeView.addListener(this);
        this.user = user;
    }

    @Override
    public User getLoggedUser(token token) {
        return Organizer.getUserByToken(token);
    }

    @Override
    public List<Event> getEvents(token token) throws IOException, InterruptedException {
        // TODO
        return EventController.getEvents(token);
    }

    @Override
    public void deleteEvent(token token, Event event) {
        // TODO
    }

    @Override
    public List<User> admin_getUsers(token token) {
        System.out.println("via user: " + this.user.admin_getUsers(token));
        return this.user.admin_getUsers(token);
    }

    @Override
    public void admin_editUser(token token, User user, String newUsername, String newName, String newEmail) {
        this.user.admin_edit(token, user.id, newEmail, newUsername, newName);
    }

    @Override
    public void admin_deleteUser(token token, User user) {
        this.user.admin_delete(token , user.id);
    }
}
