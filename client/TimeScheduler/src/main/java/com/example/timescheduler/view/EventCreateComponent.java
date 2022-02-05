package com.example.timescheduler.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class EventCreateComponent extends GridPane {

    public EventCreateComponent() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_create_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

}
