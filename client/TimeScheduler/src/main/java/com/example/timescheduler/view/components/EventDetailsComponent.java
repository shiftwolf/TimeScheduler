package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EventDetailsComponent extends GridPane {

    HomeView homeView;
    List<String> participants;
    List<String> attachments;

    @FXML
    VBox participantsSection;
    @FXML
    VBox attachmentsSection;

    public EventDetailsComponent(HomeView homeView) {
        this.homeView = homeView;

        // for debugging
        participants = Arrays.asList("Sarah Boeckel", "Timo Wolf", "Bob Tester", "Ember");
        attachments = Arrays.asList("01.txt", "02.txt", "03.txt");

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
    public void initialize() {
        // add participants to participantsSection
        for (String item : participants) {
            Label participant = new Label(item);
            participantsSection.getChildren().add(participant);
            VBox.setMargin(participant, new Insets(6, 15, 0, 15));
        }

        // add attachments to attachmentsSection
        for (String item : attachments) {
            AttachmentComponent attachment = new AttachmentComponent(this, item);
            attachmentsSection.getChildren().add(attachment);
            VBox.setMargin(attachment, new Insets(6, 15, 0, 15));
        }
    }

    @FXML
    public void onEdit() {
        homeView.getMainGrid().add(homeView.getEventEditComponent(), 3, 0);
        homeView.getMainGrid().getChildren().remove(this);
        homeView.setIsEdit(true);
    }

    @FXML
    public void onDelete() {
        // TODO
    }
}
