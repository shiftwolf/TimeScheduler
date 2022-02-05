package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class EventDetailsComponent extends GridPane {

    HomeView homeView;

    @FXML
    Button editButton;

    public EventDetailsComponent(HomeView homeView) {
        this.homeView = homeView;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_details_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void onEdit() {
        homeView.mainGrid.add(homeView.eventEdit, 3, 0);
        homeView.mainGrid.getChildren().remove(this);
    }

    @FXML
    public void onDelete() {
        // TODO
    }
}
