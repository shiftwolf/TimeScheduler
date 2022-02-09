package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.Organizer;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.LoginView;

public class LoginPresenter implements LoginViewListener {

    private final LoginView loginView;
    private final User user;

    public LoginPresenter(LoginView loginView, User user) {
        this.loginView = loginView;
        this.loginView.addListener(this);
        this.user = user;
    }

    @Override
    public token onLogin(String username, String password) {
        return Organizer.login(username, password);
    }
}
