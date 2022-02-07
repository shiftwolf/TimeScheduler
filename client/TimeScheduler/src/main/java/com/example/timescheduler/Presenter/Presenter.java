package com.example.timescheduler.Presenter;

import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.LoginView;

public class Presenter implements ViewListener {

    private final LoginView loginView;
    private final User user;


    public Presenter(LoginView loginView, User user) {
        this.loginView = loginView;
        this.loginView.addListener(this);
        this.user = user;
    }

    @Override
    public void onLogin(String username, String password) {
        // TODO
        
    }
}
