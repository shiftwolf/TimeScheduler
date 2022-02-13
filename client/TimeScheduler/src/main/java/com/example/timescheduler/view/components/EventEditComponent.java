package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.view.HomeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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

//    @FXML
//    VBox participantsSection;
//    @FXML
//    VBox attachmentsSection;

    public EventEditComponent(HomeView homeView) {
        this.homeView = homeView;

        // for debugging
//        participants = Arrays.asList("Sarah Boeckel", "Timo Wolf", "Bob Tester", "Ember");
//        attachments = Arrays.asList("01.txt", "02.txt", "03.txt");

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
        // TODO: data to presenter & keep track of selected event

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
        // remove
    }

    public void setInitialValues(Event event) {
        nameField.setText(event.getName());
        priorityPicker.setValue(convertToString(event.getPriority()));
        datePicker.setValue(convertToLocalDate(event.getDate()));

        // TODO: fix disappearing values
        timePicker.setValue(getTimeFormatted(event.getDate()));
        System.out.println("timepicker value: " + getTimeFormatted(event.getDate()));

        // convert the duration to the right format before setting the values
        String durationString = formatDurationNumbersOnly(event);
        String[] durationSegments = durationString.split(" ");

        durationHPicker.setValue(durationSegments[0]);

        // TODO: fix disappearing values
        if (durationSegments.length == 2) {
            durationMinPicker.setValue(durationSegments[1]);
        }


        // TODO
//        reminderPicker.setValue();

        locationField.setText(event.getLocation());

        // TODO: use actual types
//        // add participants to participantsSection
//        for (String item : participants) {
//            ParticipantComponent participant = new ParticipantComponent(this, item);
//            participantsSection.getChildren().add(participant);
//            VBox.setMargin(participant, new Insets(6, 15, 0, 15));
//        }

        // add attachments to attachmentsSection
//        for (String item : attachments) {
//            AttachmentEditComponent attachment = new AttachmentEditComponent(this, item);
//            attachmentsSection.getChildren().add(attachment);
//            VBox.setMargin(attachment, new Insets(6, 15, 0, 15));
//        }
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

    public String getTimeFormatted(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String time = format.format(date);
        return time;
    }

    public String formatDurationNumbersOnly(Event event) {
        long milliseconds = event.getDuration().getTime();
        // millisec -> hours
        long hours = (((milliseconds/1000)/60)/60);
        // millisec -> minutes
        long minutes = (((milliseconds/1000)/60)%60);

        String durationString = "";
        if (hours > 0) {
            durationString = String.format("%d ", hours);
        } else {
            durationString = "0";
        }
        if (minutes > 0){
            durationString = durationString + String.format("%d", minutes);
        } else {
            durationString = durationString + "0";
        }

        return durationString;
    }

//    public VBox getParticipantsSection() { return participantsSection; }
//
//    public VBox getAttachmentsSection() { return attachmentsSection; }
}
