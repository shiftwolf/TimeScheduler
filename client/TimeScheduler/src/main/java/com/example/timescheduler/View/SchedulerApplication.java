package com.example.timescheduler.View;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
        registrationScene = new Scene(registrationLoader.load(), 450, 450);

        FXMLLoader mainLoader = new FXMLLoader(SchedulerApplication.class.getResource("home_view.fxml"));
        homeScene = new Scene(mainLoader.load(), 1100, 720);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Time Scheduler");
        primaryStage.show();
    }
}
