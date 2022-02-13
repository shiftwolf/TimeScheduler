package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface HomeViewListener {

    void setUser(User loggedUser);

    byte[] getSchedule(token token);

    User getLoggedUser(token token);

    Event getEventById(token token, long eventId);

    List <Event> getEvents(token token) throws IOException, InterruptedException;

    void createEvent(String name,
                     Date date,
                     Date duration,
                     String location,
                     Event.priorities priority,
                     String[] participantMails,
                     Date reminder,
                     token token);

    String editEvent(Long eventId,
                     String name,
                     Date date,
                     Date duration,
                     String location,
                     Event.priorities priority,
                     Date reminder,
                     token token);

    void deleteEvent(token token, Event event);

    int addParticipant(token token, String email, Long eventId);

    int removeParticipant(token token, long eventId, long userId);

    int addAttachment(token token, long eventId, String path);

    byte[] downloadAttachment(token token, long attId);

    void logout(token token);

    // admin only:

    List<User> admin_getUsers(token token);

    void admin_editUser(token token, User user, String newUsername, String newName, String newEmail);

    void admin_deleteUser(token token, User user);

}
