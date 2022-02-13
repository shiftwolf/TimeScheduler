package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class EventDetailsComponent extends GridPane {

    HomeView homeView;
    List<User> participants;
    List<String> attachments;

    @FXML
    Label name;
    @FXML
    Label date;
    @FXML
    Label duration;
    @FXML
    Label reminder;
    @FXML
    Label eventLocation;
    @FXML
    VBox participantsSection;
    @FXML
    VBox attachmentsSection;

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
    public void initialize() {
        // add participants to participantsSection
//        for (User user : participants) {
//            Label participant = new Label(user.getName());
//            participantsSection.getChildren().add(participant);
//            VBox.setMargin(participant, new Insets(6, 15, 0, 15));
//        }

        // add attachments to attachmentsSection
//        for (String item : attachments) {
//            AttachmentComponent attachment = new AttachmentComponent(this, item);
//            attachmentsSection.getChildren().add(attachment);
//            VBox.setMargin(attachment, new Insets(6, 15, 0, 15));
//        }
    }

    @FXML
    public void onEdit() {
        homeView.getMainGrid().add(homeView.getEventEditComponent(), 1, 0);
        homeView.getMainGrid().getChildren().remove(this);

        // initialize the values of the input controls
        homeView.getEventEditComponent().setInitialValues(homeView.getSelectedEvent());
    }

    @FXML
    public void onDelete() {
        homeView.notifyOnDeleteEvent();
    }

    @FXML
    public void onAddParticipant() {
        // TODO
        // check if participant exists
    }

    @FXML
    public void onAddAttachment(ActionEvent actionEvent) throws IOException {
        // TODO: unfinished
        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog((Stage) ((Node)actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            System.out.println(file.toPath());
            Path path = file.toPath();

            //File in bytecode
            byte[] bytes = Files.readAllBytes(path);
        }
    }

    public void setDetails(Event event) {
        name.setText(event.getName());
        date.setText(homeView.formatDate(event.getDate()));
        duration.setText(homeView.formatDuration(event));

        reminder.setText(String.format("%s before the event", homeView.convertReminderToString(event)));

        eventLocation.setText(event.getLocation());
        // TODO: participants, attachments
    }

    public void loadParticipantComponents(String email) {
        // clear participants section first
        System.out.println("before clear: " + participantsSection.getChildren());
        participantsSection.getChildren().clear();
        System.out.println("after clear: " + participantsSection.getChildren());

        // load participant components
        for (User user : participants) {
            ParticipantComponent participantComponent = new ParticipantComponent(this, user);
            participantsSection.getChildren().add(participantComponent);
            VBox.setMargin(participantComponent, new Insets(6, 15, 0, 15));
        }
    }
}
