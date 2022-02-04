package com.example.timescheduler.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SchedulerApplication extends Application {
    // TODO: new stage for main application
    static Scene loginScene, registrationScene, homeScene;

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
