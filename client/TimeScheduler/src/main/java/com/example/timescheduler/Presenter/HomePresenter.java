package com.example.timescheduler.Presenter;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Controller.UserController;
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
    public List<User> getUsers(token token) throws IOException, InterruptedException {
        
        return UserController.getUsers(token);
    }
}
