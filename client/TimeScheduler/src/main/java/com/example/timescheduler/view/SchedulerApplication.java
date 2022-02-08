package com.example.timescheduler.view;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.Presenter.HomePresenter;
import com.example.timescheduler.Presenter.LoginPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

// TODO: delete token when close app

public class SchedulerApplication extends Application {

    public static token token = new token();

    static Scene loginScene, registrationScene, homeScene;

    public SchedulerApplication() {
        LoginView loginView = new LoginView();
        User loginUser = new User();
        new LoginPresenter(loginView, loginUser);

        HomeView homeView = new HomeView();
        User user = new User();
        new HomePresenter(homeView, user);
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader(SchedulerApplication.class.getResource("login_view.fxml"));
        loginScene = new Scene(loginLoader.load(), 450, 380);

        FXMLLoader registrationLoader = new FXMLLoader(SchedulerApplication.class.getResource("registration_view.fxml"));
        registrationScene = new Scene(registrationLoader.load(), 550, 550);

        FXMLLoader mainLoader = new FXMLLoader(SchedulerApplication.class.getResource("home_view.fxml"));
        homeScene = new Scene(mainLoader.load(), 1100, 720);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Time Scheduler");
        primaryStage.show();
    }

}
