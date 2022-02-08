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

    HomeView homeView;
    User user;

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
        // keep track of selected user
        homeView.setSelectedUser(user);

        // load UserEditComponent
        if (!homeView.isInGrid(homeView.getUserEdit())) {
            homeView.getMainGrid().add(homeView.getUserEdit(), 1, 0);
        }

        // set user data in UserEditComponent
        homeView.getUserEdit().usernameField.setText(user.getUsername());
        homeView.getUserEdit().nameField.setText(user.getName());
        homeView.getUserEdit().emailField.setText(user.getEmail());
    }

    @FXML
    public void onDelete(ActionEvent event) {
        // TODO
    }

}
