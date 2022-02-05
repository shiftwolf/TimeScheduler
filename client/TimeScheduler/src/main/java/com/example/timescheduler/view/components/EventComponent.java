package com.example.timescheduler.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class EventComponent extends HBox {
    @FXML
    Label name;
    @FXML
    Label details;
    @FXML
    Button detailsButton;
    
    public EventComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void onDetailsButton() {
        // TODO
    }

}