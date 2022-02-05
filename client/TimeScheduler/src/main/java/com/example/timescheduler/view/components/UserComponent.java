package com.example.timescheduler.view.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserComponent extends GridPane {

    @FXML
    Label userName;
    @FXML
    Label name;

    public UserComponent() {
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
    public void onEdit(ActionEvent event) {
        // TODO
    }

    @FXML
    public void onDelete(ActionEvent event) {
        // TODO
    }

}
