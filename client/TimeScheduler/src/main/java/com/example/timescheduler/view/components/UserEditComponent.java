package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserEditComponent extends GridPane {
    HomeView homeView;

    @FXML
    TextField usernameField;
    @FXML
    TextField nameField;
    @FXML
    TextField emailField;

    public UserEditComponent(HomeView homeView) {
        this.homeView = homeView;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user_edit_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void onSave() {
        // notify listener
        homeView.notifyOnEditUser(
                usernameField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim());

        // stop showing the edit options
        homeView.getMainGrid().getChildren().remove(this);
    }

    @FXML
    public void onDeleteUser() {
        // TODO
    }

    @FXML
    public void onCancel() {
        homeView.getMainGrid().getChildren().remove(this);
    }
}
