package com.example.timescheduler.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EventComponent extends GridPane {
    @FXML
    Label eventName;
    @FXML
    Label eventDetails;
    
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

    public String getName() {
        return nameTextProperty().get();
    }

    public void setName(String value) {
        nameTextProperty().set(value);
    }

    public StringProperty nameTextProperty() {
        return eventName.textProperty();
    }

    public String getDetails() {
        return detailsTextProperty().get();
    }

    public void setDetails(String value) {
        detailsTextProperty().set(value);
    }

    public StringProperty detailsTextProperty() {
        return eventDetails.textProperty();
    }

}