package com.example.timescheduler.view.components;

import com.example.timescheduler.view.AdminView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserEditComponent extends GridPane {

    AdminView adminView;

    public UserEditComponent(AdminView adminView) {
        this.adminView = adminView;

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
        // TODO: unfinished

        if (adminView.getIsEditVisible()) {
            adminView.getMainGrid().getChildren().remove(adminView.getUserEdit());
            adminView.setIsEditVisible(false);
        }
    }

    @FXML
    public void onDeleteUser() {
        // TODO
    }

}
