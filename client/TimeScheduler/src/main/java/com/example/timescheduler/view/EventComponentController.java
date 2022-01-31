package com.example.timescheduler.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EventComponentController {
    @FXML
    Label eventName;
    @FXML Label eventDetails;

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
