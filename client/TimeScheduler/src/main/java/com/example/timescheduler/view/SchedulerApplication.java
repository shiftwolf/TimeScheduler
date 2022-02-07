package com.example.timescheduler.view;

import com.example.timescheduler.Model.User;
import com.example.timescheduler.Presenter.Presenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SchedulerApplication extends Application {

    static Scene loginScene, registrationScene, homeScene, adminScene;

    public SchedulerApplication() {
        LoginView loginView = new LoginView();
        User user = new User();
        new Presenter(loginView, user);
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loginLoader = new FXMLLoader(SchedulerApplication.class.getResource("login_view.fxml"));
        loginScene = new Scene(loginLoader.load(), 450, 380);

        FXMLLoader registrationLoader = new FXMLLoader(SchedulerApplication.class.getResource("registration_view.fxml"));
        registrationScene = new Scene(registrationLoader.load(), 550, 550);

        FXMLLoader mainLoader = new FXMLLoader(SchedulerApplication.class.getResource("home_view.fxml"));
        homeScene = new Scene(mainLoader.load(), 1100, 720);

        FXMLLoader adminLoader = new FXMLLoader(SchedulerApplication.class.getResource("admin_view.fxml"));
        adminScene = new Scene(adminLoader.load(), 1100, 720);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Time Scheduler");
        primaryStage.show();
    }

    public static void initializeDropDownMenus(ComboBox timePicker, ComboBox durationHPicker, ComboBox durationMinPicker) {
        // set time values from 00:00 to 23:55
        for (int h = 0; h < 24; h++) {
            for (int min = 0; min < 60; min += 5) {
                String hour = SchedulerApplication.formatTime(h);
                String minutes = SchedulerApplication.formatTime(min);
                timePicker.getItems().add(String.format("%s:%s", hour, minutes));
            }
        }

        // set hour values of the duration picker
        for (int h = 0; h < 24; h++) {
            durationHPicker.getItems().add(String.valueOf(h));
        }

        // set minutes values of the duration picker
        for (int min = 5; min < 60; min += 5) {
            durationMinPicker.getItems().add(String.valueOf(min));
        }
    }

    public static String formatTime(int num) {
        if (num <= 9) {
            return String.format("0%d", num);
        } else {
            return String.valueOf(num);
        }
    }

}
