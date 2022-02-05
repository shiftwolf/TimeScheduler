package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class EventEditComponent extends GridPane {

    HomeView homeView;

    @FXML
    Button saveButton;

    public EventEditComponent(HomeView homeView) {
        this.homeView = homeView;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_edit_component.fxml"));
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
        homeView.mainGrid.add(homeView.eventDetails, 3, 0);
        homeView.mainGrid.getChildren().remove(this);
    }

}
