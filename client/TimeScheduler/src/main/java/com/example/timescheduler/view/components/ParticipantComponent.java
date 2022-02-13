package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ParticipantComponent extends HBox {

    EventDetailsComponent eventDetailsComponent = null;
    String email;

    @FXML
    Label participant;

    public ParticipantComponent(EventDetailsComponent eventDetailsComponent, String email) {
        this.eventDetailsComponent = eventDetailsComponent;
        this.email = email;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("participant_component.fxml"));
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
        participant.setText(email);
    }

    @FXML
    public void onRemove() {
        // TODO: notify listener

        // update in local list & UI
        eventDetailsComponent.participantsSection.getChildren().remove(this);
        eventDetailsComponent.participantsEmails.remove(email);
    }
}
