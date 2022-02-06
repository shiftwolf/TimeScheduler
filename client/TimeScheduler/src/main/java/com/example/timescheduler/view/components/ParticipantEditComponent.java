package com.example.timescheduler.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ParticipantEditComponent extends HBox {

    EventEditComponent eventEditComponent;
    String name;

    @FXML
    Label participant;

    public ParticipantEditComponent(EventEditComponent eventEditComponent, String name) {
        this.eventEditComponent = eventEditComponent;
        this.name = name;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("participant_edit_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize() {
        participant.setText(name);
    }

    @FXML
    public void onRemove() {
        // TODO: save change in edit view
        eventEditComponent.getParticipantsSection().getChildren().remove(this);
    }

}
