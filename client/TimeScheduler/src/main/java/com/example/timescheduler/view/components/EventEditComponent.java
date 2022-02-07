package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import com.example.timescheduler.view.SchedulerApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
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

public class EventEditComponent extends GridPane {

    HomeView homeView;
    List<String> participants;
    List<String> attachments;

    @FXML
    TextField nameField;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox priorityPicker;
    @FXML
    ComboBox timePicker;
    @FXML
    ComboBox durationHPicker;
    @FXML
    ComboBox durationMinPicker;
    @FXML
    TextField locationField;
    @FXML
    VBox participantsSection;
    @FXML
    VBox attachmentsSection;

    public EventEditComponent(HomeView homeView) {
        this.homeView = homeView;

        // for debugging
        participants = Arrays.asList("Sarah Boeckel", "Timo Wolf", "Bob Tester", "Ember");
        attachments = Arrays.asList("01.txt", "02.txt", "03.txt");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_edit_component.fxml"));
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
        SchedulerApplication.initializeDropDownMenus(timePicker, durationHPicker, durationMinPicker);

        // add participants to participantsSection
        for (String item : participants) {
            ParticipantEditComponent participant = new ParticipantEditComponent(this, item);
            participantsSection.getChildren().add(participant);
            VBox.setMargin(participant, new Insets(6, 15, 0, 15));
        }

        // add attachments to attachmentsSection
        for (String item : attachments) {
            AttachmentEditComponent attachment = new AttachmentEditComponent(this, item);
            attachmentsSection.getChildren().add(attachment);
            VBox.setMargin(attachment, new Insets(6, 15, 0, 15));
        }
    }

    @FXML
    public void onSave() {
        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 3, 0);
        homeView.getMainGrid().getChildren().remove(this);
        homeView.setIsEdit(false);
    }

    @FXML
    public void onCancel() {
        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 3, 0);
        homeView.getMainGrid().getChildren().remove(this);
        homeView.setIsEdit(false);
    }

    @FXML
    public void onAddParticipant() {
        // TODO
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

    public VBox getParticipantsSection() { return participantsSection; }

    public VBox getAttachmentsSection() { return attachmentsSection; }
}
