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

    @FXML
    public void initialize() {
        inputFields = Arrays.asList(username, email, name, password1, password2);
        infoLabels = Arrays.asList(usernameInfo, passwordInfo, confirmPasswordInfo);

        password1.textProperty().bindBidirectional(password1Visible.textProperty());
        password2.textProperty().bindBidirectional(password2Visible.textProperty());

        usernameInfo.managedProperty().bind(usernameInfo.visibleProperty());
        passwordInfo.managedProperty().bind(passwordInfo.visibleProperty());
        confirmPasswordInfo.managedProperty().bind(confirmPasswordInfo.visibleProperty());
    }

    @FXML
    protected void onSignUp(ActionEvent event) throws IOException {
        // TODO: trim spaces

        boolean isValid = validateSignUp();

        if (isValid) {
//            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
//            stage.setScene(SchedulerApplication.homeScene);
//            stage.setX(200);
//            stage.setY(100);

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
        }
    }

    private boolean validateSignUp() {
        resetTextFields();

        String usernameRegex = "^[\\w_-]*$";
        String nameRegex = "^[a-zA-Z\\s]*$";
        String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
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

        // for debugging false
        return false;
    }

    @FXML
    protected void onCancel(ActionEvent event) {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
        resetGUI();
    }

    @FXML
    protected void onInfoToggle() {
        usernameInfo.setVisible(infoToggle.isSelected());
        passwordInfo.setVisible(infoToggle.isSelected());
        confirmPasswordInfo.setVisible(infoToggle.isSelected());
    }

    @FXML
    protected void onPasswordToggle() {
        if (passwordToggle.isSelected()) {
            password1Visible.toFront();
            password2Visible.toFront();
            password1.toBack();
            password2.toBack();
        } else {
            password1.toFront();
            password2.toFront();
            password1Visible.toBack();
            password2Visible.toBack();
        }
    }

    private void resetTextFields() {
        for (TextField input : inputFields) {
            input.setStyle("-fx-border-color: #d5d5d5;");
        }
        for (Label info : infoLabels) {
            info.setVisible(false);
        }
    }

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
