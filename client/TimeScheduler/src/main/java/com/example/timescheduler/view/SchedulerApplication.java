package com.example.timescheduler.view;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.Presenter.HomePresenter;
import com.example.timescheduler.Presenter.LoginPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// TODO: reset token when close app


/**
 * This class is the starting point of the client application.
 */
public class SchedulerApplication extends Application {

    public static token token = new token();
    static LoginView loginView = new LoginView();
    static HomeView homeView = new HomeView();

    static Scene loginScene, registrationScene;

    /**
     * The constructor creates the presenters for each view
     */
    public SchedulerApplication() {
        User loginUser = new User();
        new LoginPresenter(loginView, loginUser);

        User user = new User();
        new HomePresenter(homeView, user);
    }

    public static void main(String[] args) { launch(args); }

    public void start(Stage primaryStage) throws IOException {
        // initializes the Scenes for the first window
        FXMLLoader loginLoader = new FXMLLoader(SchedulerApplication.class.getResource("login_view.fxml"));
        loginScene = new Scene(loginLoader.load(), 450, 380);

        FXMLLoader registrationLoader = new FXMLLoader(SchedulerApplication.class.getResource("registration_view.fxml"));
        registrationScene = new Scene(registrationLoader.load(), 550, 550);

        // sets the content of the window
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Time Scheduler");

        // displays the window
        primaryStage.show();
    }
}
