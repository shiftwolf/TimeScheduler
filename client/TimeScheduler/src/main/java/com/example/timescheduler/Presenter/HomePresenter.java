package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.Organizer;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;
import com.example.timescheduler.view.SchedulerApplication;

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
    public Event getEventById(token token, long eventId) {
        return user.getEventById(token, eventId);
    }

    @Override
    public List<Event> getEvents(token token) {
        return user.getEvents(token);
    }

    @Override
    public void createEvent(String name, Date date, Date duration, String location, Event.priorities priority, String[] participantMails, Date reminder, token token) {
        user.addEvent(name, date, duration, location, priority, participantMails, reminder, token);
    }

    @Override
    public String editEvent(Long eventId, String name, Date date, Date duration, String location, Event.priorities priority, Date reminder, token token) {
        return user.editEvent(eventId, name, date, duration, location, priority, reminder, SchedulerApplication.token);
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
    public int removeParticipant(token token, long eventId, long userId) {
        return user.removeParticipant(token, eventId, userId);
    }

    @Override
    public int addAttachment(token token, long eventId, String path) {
        return user.uploadAtt(token, eventId, path);
    }

    @Override
    public byte[] downloadAttachment(token token, long attId) {
        return user.downloadAtt(token, attId);
    }

    @Override
    public void logout(token token) {
        Organizer.logout(token);
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
