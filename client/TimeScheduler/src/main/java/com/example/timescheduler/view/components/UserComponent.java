package com.example.timescheduler.view.components;

import com.example.timescheduler.view.AdminView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserComponent extends GridPane {

    AdminView adminView;

    @FXML
    Label userName;
    @FXML
    Label name;

    public UserComponent(AdminView adminView) {
        this.adminView = adminView;

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
        // TODO: unfinished

        // load UserEditComponent
        if (!adminView.getIsEditVisible()) {
            adminView.getMainGrid().add(adminView.getUserEdit(), 1, 0);
            adminView.setIsEditVisible(true);
        }
    }

    @FXML
    public void onDelete(ActionEvent event) {
        // TODO
    }

}
