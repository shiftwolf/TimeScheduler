package com.example.timescheduler.Presenter;

import com.example.timescheduler.Model.Organizer;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.RegistrationView;

public class RegistrationPresenter implements RegistrationViewListener {

    private final RegistrationView registrationView;
    private final User user;

    public RegistrationPresenter(RegistrationView registrationView, User user) {
        this.registrationView = registrationView;
        this.registrationView.addListener(this);
        this.user = user;
    }

    @Override
    public String createUser(String name, String email, String username, String password) {
        return Organizer.createUser(name, email, username, password);
    }
}
