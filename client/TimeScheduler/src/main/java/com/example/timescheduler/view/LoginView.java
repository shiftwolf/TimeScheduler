package com.example.timescheduler.view;

import com.example.timescheduler.Presenter.LoginViewListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LoginView {
    private static final ArrayList<LoginViewListener> listeners = new ArrayList<>();

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
        boolean hasFailed = false;

        // notify listeners
        for (final LoginViewListener listener : listeners) {
            try {
                listener.onLogin(username.getText().trim(), password.getText().trim());
            } catch (Exception e) {
                hasFailed = true;
            }
        }

        // update GUI
        if (hasFailed) {
            loginError.setVisible(true);
        } else {
            // navigate to Home
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

    public void addListener(final LoginViewListener listener) {
        listeners.add(listener);
    }
}