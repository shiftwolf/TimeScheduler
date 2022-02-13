package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.AttachmentsInfo;
import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventDetailsComponent extends GridPane {

    HomeView homeView;
    List<String> participantsEmails = new ArrayList<String>();
    List<String> attachmentsNames = new ArrayList<String>();

    List<AttachmentsInfo> attachments;

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
    TextField newParticipantField;
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
    public void onEdit() {
        if (homeView.getSelectedEvent() != null) {
            homeView.getMainGrid().add(homeView.getEventEditComponent(), 1, 0);
            homeView.getMainGrid().getChildren().remove(this);

            // initialize the values of the input controls
            homeView.getEventEditComponent().setInitialValues(homeView.getSelectedEvent());
        }
    }

    @FXML
    public void onDelete() {
        homeView.deleteSelectedEvent();
    }

    @FXML
    public void onAddParticipant() {
        String newParticipantEmail = newParticipantField.getText().trim();

        // notify listener
        int response = homeView.notifyOnAddParticipant(newParticipantEmail);

        // check if was successful
        if (response == 0) {
            // update participants list
            participantsEmails.add(newParticipantEmail);
            // update GUI
            loadParticipantComponents();
            // clear textField
            newParticipantField.clear();
        } else if (response == 1) {
            System.out.println("is already participating");
        } else if (response == 2) {
            System.out.println("participant doesn't exist");
        } else {System.out.println("add participant didn't work");}
    }

    @FXML
    public void onAddAttachment(ActionEvent actionEvent) {
        int response;

        FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog((Stage) ((Node)actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            String filePath = file.getAbsolutePath();
            // notify listener
            response = homeView.notifyOnAddAttachment(filePath);

            // update local attachment names list and GUI
            if (response == 0) {
                // file added successfully => update attachments list
                attachmentsNames.add(file.getName());
                // update GUI
                loadAttachmentComponents();
            }
        }
    }

    public void setDetails(Event event) {
        if (event != null) {
            name.setText(event.getName());
            date.setText(homeView.formatDate(event.getDate()));
            duration.setText(homeView.formatDuration(event));
            reminder.setText(String.format("%s before the event", homeView.convertReminderToString(event)));
            eventLocation.setText(event.getLocation());

            // clear old participants
            participantsEmails.clear();

            // load participants
            for (User user : event.getParticipantsEntities()) {
                participantsEmails.add(user.getEmail());
            }
            if (!participantsEmails.isEmpty() ) {
                loadParticipantComponents();
            }

            // load attachments
            loadAttachmentComponents();

        } else {
            // debug
            System.out.println("Can't load details because user has no events");
        }

    }

    public void loadParticipantComponents() {
        // clear participants section first
        participantsSection.getChildren().clear();

        Event event = homeView.notifyOnGetEventById(homeView.getSelectedEvent().getId());

        // load participant components
        for (User user : event.getParticipantsEntities()) {
            ParticipantComponent participantComponent = new ParticipantComponent(this, user);
            participantsSection.getChildren().add(participantComponent);
            VBox.setMargin(participantComponent, new Insets(6, 15, 0, 15));
        }
    }

    public void loadAttachmentComponents() {
        // clear attachments section first
        attachmentsSection.getChildren().clear();

        // make sure data of current event is up-to-date: notify listener
        Event event = homeView.notifyOnGetEventById(homeView.getSelectedEvent().getId());
        attachments = List.of(event.getAttachments());

        // load participant components
        for (AttachmentsInfo file : attachments) {
            AttachmentComponent attachmentComponent = new AttachmentComponent(this, file);
            attachmentsSection.getChildren().add(attachmentComponent);
            VBox.setMargin(attachmentComponent, new Insets(6, 15, 0, 15));
        }
    }
}
