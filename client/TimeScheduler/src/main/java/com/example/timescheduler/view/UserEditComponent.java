package com.example.timescheduler.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserEditComponent extends GridPane {

    public UserEditComponent() {
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
        // TODO
    }

    @FXML
    public void onDeleteUser() {
        // TODO
    }

}
