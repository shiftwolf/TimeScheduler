package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.EventController;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.Organizer;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class HomePresenter implements HomeViewListener {

    private final HomeView homeView;
    private User user;

    public HomePresenter(HomeView homeView, User user) {
        this.homeView = homeView;
        this.homeView.addListener(this);
        this.user = user;
    }

    public void setUser(User loggedUser) {
        this.user = loggedUser;
        System.out.println("loggedUser: " + this.user);
    }

    @Override
    public byte[] getSchedule(token token) {
        return user.getWeeklySchedule(token);
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
    public void createEvent(String name, Date date, Date duration, String location, Event.priorities priority, String[] participantMails, Date reminder, token token) {
        user.addEvent(name, date, duration, location, priority, participantMails, reminder, token);
    }

    @Override
    public void deleteEvent(token token, Event event) {
        user.deleteEvent(token, event.getId());
    }

    @Override
    public int addParticipant(token token, String email, Long eventId) {
        return user.addParticipant(token, email, eventId);
    }

    @Override
    public List<User> admin_getUsers(token token) {
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
