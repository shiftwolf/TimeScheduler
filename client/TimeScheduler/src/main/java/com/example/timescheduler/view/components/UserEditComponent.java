package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.User;
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

        // TODO: update local list & GUI
        homeView.updateAfterEditUser();
//        homeView.updateUserComponent();

        // stop showing the edit options
        homeView.getMainGrid().getChildren().remove(this);

        // keep track of selected user & component
        homeView.setSelectedUser(null);
        homeView.setSelectedUserComponent(null);
    }

    @FXML
    public void onDeleteUser() {
        // notify listener
         homeView.notifyOnDeleteUser(homeView.getSelectedUser());

        // update local list of users & GUI
        homeView.removeUser();
        homeView.removeUserComponent();
        homeView.getMainGrid().getChildren().remove(this);

        // keep track of selected user & component
        homeView.setSelectedUser(null);
        homeView.setSelectedUserComponent(null);
    }

    @FXML
    public void onCancel() {
        // keep track of selected user & component
        homeView.setSelectedUser(null);
        homeView.setSelectedUserComponent(null);

        homeView.getMainGrid().getChildren().remove(this);
    }

    public void initializeValues() {
        User user = homeView.getSelectedUser();

        usernameField.setText(user.getUsername());
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
    }
}
