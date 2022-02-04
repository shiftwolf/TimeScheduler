package com.example.timescheduler.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RegistrationView {
    @FXML
    TextField username;
    @FXML
    TextField email;
    @FXML
    TextField name;
    @FXML
    PasswordField password1;
    @FXML
    PasswordField password2;

    @FXML
    protected void onSignUp(ActionEvent event) throws IOException {
        // TODO: check criteria + notify listener

        System.out.println("Sign up button");
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.homeScene);
        stage.setX(200);
        stage.setY(100);
    }

    @FXML
    protected void onCancel(ActionEvent event) {
        System.out.println("Back to Login button");
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }
}
