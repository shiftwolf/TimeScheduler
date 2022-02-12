package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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

        participants = List.of();
        attachments = Arrays.asList("text1.pdf", "bhkjekvc.txt", "A.txt", "ein_laengerer_titel.txt");

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
        for (User user : participants) {
            Label participant = new Label(user.getName());
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
        homeView.getMainGrid().add(homeView.getEventEditComponent(), 1, 0);
        homeView.getMainGrid().getChildren().remove(this);

        // initialize the values of the input controls
        homeView.getEventEditComponent().setInitialValues(homeView.getSelectedEvent());
    }

    @FXML
    public void onDelete() {
        homeView.notifyOnDelete(homeView.getSelectedEvent());
    }

    public void setDetails(Event event) {
        name.setText(event.getName());
        date.setText(homeView.formatDate(event.getDate()));
        duration.setText(homeView.formatDuration(event));

        // TODO
        reminder.setText(String.format("%s before the event", homeView.convertReminderToString(event)));
        System.out.println(homeView.convertReminderToString(event));

        eventLocation.setText(event.getLocation());
        // TODO: participants, attachments
    }
}
