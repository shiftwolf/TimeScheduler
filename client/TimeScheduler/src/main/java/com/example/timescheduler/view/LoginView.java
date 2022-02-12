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

/**
 * This class is the controller of the login_view.fxml.
 * It handles the user interactions with the login window.
 */
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

    /**
     * This function binds certain properties of the GUI elements when the view is initialized.
     */
    @FXML
    public void initialize() {
        // sync the text in the PasswordField and the TextField. This is necessary for the visibility toggle.
        password.textProperty().bindBidirectional(passwordVisible.textProperty());
        // loginError text is "removed" when it is not visible (other components treat it like it doesn't exist)
        loginError.managedProperty().bind(loginError.visibleProperty());
    }

    /**
     * This function is called when the login button is pressed.
     * It notifies the LoginView listener to check whether the login was successful and loads the main application.
     * @param event Event that represents the action that the corresponding button has been pressed.
     * @throws IOException Exception that occurs if an error arises in LoginViewListener.onLogin or FXMLLoader.load.
     * @throws InterruptedException Exception that occurs a thread is interrupted in LoginViewListener.onLogin.
     */
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
                // store the token for this session
                SchedulerApplication.token = token;
            }
        }

        // update GUI
        if (hasFailed) {
            // notify the user that the login has failed
            loginError.setVisible(true);
        } else {
            // new window for main app where HomeView will be loaded
            Parent fxml = FXMLLoader.load(getClass().getResource("home_view.fxml"));
            Scene scene = new Scene(fxml);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // configure the size and position of the window
            appStage.setScene(scene);
            appStage.setWidth(1100);
            appStage.setHeight(720);
            appStage.setX(200);
            appStage.setY(100);
            appStage.show();
            appStage.centerOnScreen();
        }
    }

    /**
     * This function is called when the sign up button is pressed and loads the registration scene.
     * @param event Event that represents the action that the corresponding button has been pressed.
     */
    @FXML
    protected void onSignUp(ActionEvent event) {
        resetGUI();

        // load the registration scene
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.registrationScene);
        stage.setWidth(550);
        stage.setHeight(510);
        stage.centerOnScreen();
    }

    /**
     * This function is called when the password toggle button is pressed.
     * It switches between showing and hiding the current value of the password field.
     */
    @FXML
    protected void onPasswordToggle() {
        // checks what is the current state of the password field
        if (passwordToggle.isSelected()) {
            passwordVisible.toFront();
            password.toBack();
        } else {
            password.toFront();
            passwordVisible.toBack();
        }
    }

    /**
     * This function resets the GUI. It clears the text fields and hides the password and user notifications.
     */
    private void resetGUI() {
        password.clear();
        passwordVisible.clear();
        username.clear();

        password.toFront();
        passwordVisible.toBack();
        loginError.setVisible(false);
    }

    /**
     * This function adds a listener to the LoginView which will from then on be notified when the user interacts with the GUI.
     * @param listener Listener that implements the interface LoginViewListener (presenter).
     */
    public void addListener(final LoginViewListener listener) {
        listeners.add(listener);
    }
}