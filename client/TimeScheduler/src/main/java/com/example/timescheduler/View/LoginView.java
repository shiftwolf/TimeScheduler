package com.example.timescheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class LoginView {
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    TextField passwordVisible;
    @FXML
    ToggleButton passwordToggle;
    @FXML
    Label loginError;

    @FXML
    public void initialize() {
        password.textProperty().bindBidirectional(passwordVisible.textProperty());
        loginError.managedProperty().bind(loginError.visibleProperty());
    }

    @FXML
    protected void onLogin(ActionEvent event) {
        // TODO

        // for UI debugging
        if (username.getText().equals("y")) {
            loginError.setVisible(true);
        } else {
            resetGUI();
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(SchedulerApplication.homeScene);
            stage.setX(200);
            stage.setY(100);
        }
    }

    @FXML
    protected void onSignUp(ActionEvent event) {
        resetGUI();
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.registrationScene);
    }

    @FXML
    protected void onPasswordToggle() {
        if (passwordToggle.isSelected()) {
            passwordVisible.toFront();
            password.toBack();
        } else {
            password.toFront();
            passwordVisible.toBack();
        }
    }

    private void resetGUI() {
        password.clear();
        passwordVisible.clear();
        username.clear();

        password.toFront();
        passwordVisible.toBack();
        loginError.setVisible(false);
    }
}