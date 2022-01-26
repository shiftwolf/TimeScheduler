package com.example.timescheduler.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginView {
    @FXML
    TextField username;
    @FXML
    PasswordField password;

    @FXML
    protected void onLogin(ActionEvent event) throws IOException {
        // TODO: notify listener

        System.out.println("Switch to home scene");
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.homeScene);
        stage.setX(200);
        stage.setY(100);
    }

    @FXML
    protected void onSignUp(ActionEvent event) {
        System.out.println("Switch to registrationScene");
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.registrationScene);
    }

    @FXML
    protected void onPasswordToggle(ActionEvent event) {
        // TODO
    }
}
