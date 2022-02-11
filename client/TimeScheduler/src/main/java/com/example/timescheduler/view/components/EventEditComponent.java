package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
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
    ComboBox reminderPicker;
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
        homeView.initializeDropDownMenus(timePicker, durationHPicker, durationMinPicker);
    }

    @FXML
    public void onSave() {
        // TODO

        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        homeView.getMainGrid().getChildren().remove(this);
    }

    @FXML
    public void onCancel() {
        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        homeView.getMainGrid().getChildren().remove(this);
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

    public void setInitialValues(Event event) {
        nameField.setText(event.getName());
        priorityPicker.setValue(convertToString(event.getPriority()));
        datePicker.setValue(convertToLocalDate(event.getDate()));
        // TODO: v
//        timePicker.setValue();
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
//        String time = format.format(date);

        locationField.setText(event.getLocation());

        // TODO: use actual types
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

    public String convertToString(Event.priorities priority) {
        switch (priority) {
            case GREEN -> { return "Low"; }
            case YELLOW -> { return "Medium"; }
            case RED -> { return "High"; }
            default -> { return ""; }
        }
    }

    public LocalDate convertToLocalDate (Date date){
        // convert date to string
        SimpleDateFormat stringFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = stringFormatter.format(date);

        // create LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        return localDate;
    }

    public VBox getParticipantsSection() { return participantsSection; }

    public VBox getAttachmentsSection() { return attachmentsSection; }
}
