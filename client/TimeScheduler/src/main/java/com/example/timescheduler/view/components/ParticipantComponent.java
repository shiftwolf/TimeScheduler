package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

/**
 * This class controls the participant component. It displays the most important participant information and
 * handles user interactions.
 */
public class ParticipantComponent extends HBox {

    EventDetailsComponent eventDetailsComponent = null;
    User user;

    @FXML
    Label participant;

    public ParticipantComponent(EventDetailsComponent eventDetailsComponent, User user) {
        this.eventDetailsComponent = eventDetailsComponent;
        this.user = user;

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
        participant.setText(user.getEmail());
    }

    @FXML
    public void onRemove() {
        // notify listener
        eventDetailsComponent.homeView.notifyOnRemoveParticipant(user);

        // update in local list & UI
        eventDetailsComponent.participantsSection.getChildren().remove(this);
        eventDetailsComponent.participantsEmails.remove(user.getEmail());

        eventDetailsComponent.loadParticipantComponents();
    }
}
