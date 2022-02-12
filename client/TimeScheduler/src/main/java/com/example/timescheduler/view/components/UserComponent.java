package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserComponent extends GridPane {

    private final HomeView homeView;
    private final User user;

    @FXML
    Label username;
    @FXML
    Label name;
    @FXML
    Label email;

    public UserComponent(HomeView homeView, User user) {
        this.homeView = homeView;
        this.user = user;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("user_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize() {
        username.setText(user.getUsername());
        name.setText(user.getName());
        email.setText(user.getEmail());
    }

    @FXML
    public void onEdit(ActionEvent event) {
        // keep track of selected user & component
        homeView.setSelectedUser(user);
        homeView.setSelectedUserComponent(this);

        // load UserEditComponent & set user data
        if (!homeView.isInGrid(homeView.getUserEdit())) {
            homeView.getMainGrid().add(homeView.getUserEdit(), 1, 0);
        }

        homeView.getUserEdit().initializeValues();

//        homeView.getUserEdit().usernameField.setText(user.getUsername());
//        homeView.getUserEdit().nameField.setText(user.getName());
//        homeView.getUserEdit().emailField.setText(user.getEmail());
    }

    @FXML
    public void onDelete() {
        // keep track of selected user & component
        homeView.setSelectedUser(user);
        homeView.setSelectedUserComponent(this);

        // notify listener
         homeView.notifyOnDeleteUser(user);

        // update local list & GUI
        homeView.removeUser();
        homeView.removeUserComponent();

        // keep track of selected user & component
        homeView.setSelectedUser(null);
        homeView.setSelectedUserComponent(null);
    }

}
