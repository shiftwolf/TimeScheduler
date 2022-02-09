package com.example.timescheduler.view;

import com.example.timescheduler.APIobjects.token;
import com.example.timescheduler.Presenter.LoginViewListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

import java.io.IOException;
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
    protected void onLogin(ActionEvent event) throws IOException, InterruptedException {
        boolean hasFailed = false;
        token token;

        // notify listeners
        for (final LoginViewListener listener : listeners) {
            token = listener.onLogin(username.getText().trim(), password.getText().trim());
            if (token == null) {
                hasFailed = true;
            } else {
                SchedulerApplication.token = token;
            }
        }

        // update GUI
        if (hasFailed) {
            loginError.setVisible(true);
        } else {
            // notify application that login was successfull
//            SchedulerApplication.onSuccessfullLogin();

            // new window for main app
            Parent fxml = FXMLLoader.load(getClass().getResource("home_view.fxml"));
            Scene scene = new Scene(fxml);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.setWidth(1100);
            appStage.setHeight(720);
            appStage.setX(200);
            appStage.setY(100);
            appStage.show();
            appStage.centerOnScreen();

            // navigate to Home
//            resetGUI();
//            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//            stage.setScene(SchedulerApplication.homeScene);
//            stage.setX(200);
//            stage.setY(100);
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