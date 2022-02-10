package com.example.timescheduler.view;

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
import java.util.Arrays;
import java.util.List;

/**
 * This class is the controller of the registration_view.fxml.
 * It handles the user interactions with the registration window.
 */
public class RegistrationView {
    @FXML TextField username;
    @FXML TextField email;
    @FXML TextField name;
    @FXML PasswordField password1;
    @FXML TextField password1Visible;
    @FXML PasswordField password2;
    @FXML TextField password2Visible;

    @FXML ToggleButton passwordToggle;
    @FXML ToggleButton infoToggle;
    @FXML Label usernameInfo;
    @FXML Label passwordInfo;
    @FXML Label confirmPasswordInfo;

    Stage stage;
    List<TextField> inputFields;
    List<Label> infoLabels;

    /**
     * This function initializes the two lists and binds certain properties of the GUI elements.
     */
    @FXML
    public void initialize() {
        // initializes the lists
        inputFields = Arrays.asList(username, email, name, password1, password2);
        infoLabels = Arrays.asList(usernameInfo, passwordInfo, confirmPasswordInfo);

        // necessary for the password visibility toggle
        password1.textProperty().bindBidirectional(password1Visible.textProperty());
        password2.textProperty().bindBidirectional(password2Visible.textProperty());

        // makes the other GUI elements treat these labels like they don't exist when they are not visible
        usernameInfo.managedProperty().bind(usernameInfo.visibleProperty());
        passwordInfo.managedProperty().bind(passwordInfo.visibleProperty());
        confirmPasswordInfo.managedProperty().bind(confirmPasswordInfo.visibleProperty());
    }

    /**
     * This function is called when the sign up button is clicked.
     * It checks if the user has entered their account data correctly.
     * @param event Event that represents the action that the corresponding button has been pressed.
     * @throws IOException Exception that occurs if an error arises in FXMLLoader.load.
     */
    @FXML
    protected void onSignUp(ActionEvent event) throws IOException {
        boolean isValid = validateSignUp();

        if (isValid) {
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
     * This function checks if the data entered by the user meets all criteria.
     * It notifies the user in case there are mistakes.
     * @return true if the sign up is valid and false if not.
     */
    private boolean validateSignUp() {
        // old error messages are cleared before the next validation starts
        resetErrorNotifications();

        // username must not contain whitespaces, can contain - and _
        String usernameRegex = "^[\\w_-]*$";
        // name can contain spaces, must not contain any special characters
        String nameRegex = "^[a-zA-Z\\s]*$";
        // regex restricts the part before and after the @.
        // No special characters, no consecutive, leading or trailing dots, restricts length after the @, ...
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        // min length: 10, no spaces, min 1 number, min 1 special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[!?@#$%^&+=])(?=\\S+$).{10,}$";

        // check for empty input fields
        for (TextField input : inputFields) {
            if (input.getText().trim().isEmpty()) {
                input.setStyle("-fx-border-color: #ad4c4c;");
            }
        }
        // check username
        if (!username.getText().trim().matches(usernameRegex)) {
            username.setStyle("-fx-border-color: #ad4c4c;");
        }
        // check name
        if (!name.getText().trim().matches(nameRegex)) {
            name.setStyle("-fx-border-color: #ad4c4c;");
        }
        // check password criteria
        // min 10 characters, no spaces, min 1 number, min 1 special character
        if (!password1.getText().trim().matches(passwordRegex)) {
            passwordInfo.setVisible(true);
            password1.setStyle("-fx-border-color: #ad4c4c;");
        }
        // check if confirm password matches
        if (!password2.getText().trim().equals(password1.getText().trim())) {
            confirmPasswordInfo.setVisible(true);
            password2.setStyle("-fx-border-color: #ad4c4c;");
        }
        // email validation
        if (!email.getText().trim().matches(emailRegex)) {
            email.setStyle("-fx-border-color: #ad4c4c;");
        }

        return false;
    }

    /**
     * This function is called when the cancel button is pressed.
     * It navigates the user back to the login window.
     * @param event Event that represents the action that the corresponding button has been pressed.
     */
    @FXML
    protected void onCancel(ActionEvent event) {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
        resetGUI();
    }

    /**
     * This function is called when the info toggle button is pressed.
     * It shows and hides information about the criteria for certain input fields.
     */
    @FXML
    protected void onInfoToggle() {
        usernameInfo.setVisible(infoToggle.isSelected());
        passwordInfo.setVisible(infoToggle.isSelected());
        confirmPasswordInfo.setVisible(infoToggle.isSelected());
    }

    /**
     * This function is called when the password toggle button is pressed.
     * It shows and hides the current value of the password field.
     */
    @FXML
    protected void onPasswordToggle() {
        if (passwordToggle.isSelected()) {
            password1Visible.toFront();
            password2Visible.toFront();
        } else {
            password1.toFront();
            password2.toFront();
        }
    }

    /**
     * This function resets the user notifications that were displayed when the user entered invalid data.
     */
    private void resetErrorNotifications() {
        for (TextField input : inputFields) {
            input.setStyle("-fx-border-color: #d5d5d5;");
        }
        for (Label info : infoLabels) {
            info.setVisible(false);
        }
    }

    /**
     * This function resets the whole registration window.
     * It clears all textfields and hides user notifications.
     */
    private void resetGUI() {
        for (TextField input : inputFields) {
            if (input.getText() != null) {
                input.setText("");
            }
            input.setStyle("-fx-border-color: #d5d5d5;");
        }
        for (Label info : infoLabels) {
            info.setVisible(false);
        }
        infoToggle.setSelected(false);
    }
}
