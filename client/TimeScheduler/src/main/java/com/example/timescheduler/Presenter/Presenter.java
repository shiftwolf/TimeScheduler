package com.example.timescheduler.Presenter;

import com.example.timescheduler.Controller.Manage;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.LoginView;
import com.example.timescheduler.view.SchedulerApplication;

import java.io.IOException;

public class Presenter implements ViewListener {

    private final LoginView loginView;
    private final User user;

    public Presenter(LoginView loginView, User user) {
        this.loginView = loginView;
        this.loginView.addListener(this);
        this.user = user;
    }

    @Override
    public void onLogin(String username, String password) throws IOException, InterruptedException {
        SchedulerApplication.token = Manage.login(username, password);
    }
}
