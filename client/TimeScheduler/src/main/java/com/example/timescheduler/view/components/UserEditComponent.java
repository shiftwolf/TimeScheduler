package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class UserEditComponent extends GridPane {

    HomeView homeView;

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
        // TODO: unfinished

        if (homeView.isInGrid(homeView.getUserEdit())) {
            homeView.getMainGrid().getChildren().remove(homeView.getUserEdit());
        }
    }

    @FXML
    public void onDeleteUser() {
        // TODO
    }

}
