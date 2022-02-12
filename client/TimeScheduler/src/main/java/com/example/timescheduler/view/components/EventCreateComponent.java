package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.view.HomeView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class EventCreateComponent extends GridPane {

    HomeView homeView;

    @FXML
    TextField nameField;
    @FXML
    ComboBox priorityPicker;
    @FXML
    DatePicker datePicker;
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
    TextField newParticipantField;
    @FXML
    VBox attachmentsSection;
    @FXML
    Label errorMessage;

    public EventCreateComponent(HomeView homeView) {
        this.homeView = homeView;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_create_component.fxml"));
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
        // reset error message
        errorMessage.setVisible(false);

        boolean isValid = checkIfInputValid();

        if (isValid) {
            // get entered name & location
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            // convert input to priorities enum
            Event.priorities priority = convertPriorityToEnum(priorityPicker.getValue().toString());
            // create date
            Date date = createDateFromInput(datePicker.getValue(), timePicker.getValue().toString());
            // create duration date
            Date duration = createDurationDate(date,
                    durationHPicker.getValue().toString(),
                    durationMinPicker.getValue().toString());

            // TODO: attachments & participants

            // update GUI
            homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
            homeView.getMainGrid().getChildren().remove(this);

            resetInputFields();
        } else {
            errorMessage.setVisible(true);
        }
    }

    public boolean checkIfInputValid() {
        if (nameField.getText() == null || nameField.getText().trim().equals("")) {
            return false;
        } else if (datePicker.getValue() == null || datePicker.getValue().toString().matches("^[0123][0-9]/[01][0-9]/[2][0][0-9][0-9]$")) {
            // TODO: could check: not in past
            System.out.println("datepicker false");
            return false;
        } else if (priorityPicker.getValue() == null || reminderPicker.getValue() == null) {
            return false;
        } else if (timePicker.getValue() == null || durationMinPicker.getValue() == null || durationHPicker.getValue() == null) {
            return false;
        } else if (locationField.getText() == null || locationField.getText().trim().equals("")) {
            return false;
        }
        // attachments and participants are optional => no check necessary
        return true;
    }

    public void resetInputFields() {
        nameField.clear();
        locationField.clear();
        priorityPicker.getSelectionModel().clearSelection();
        datePicker.setValue(null);
        timePicker.getSelectionModel().clearSelection();
        durationHPicker.getSelectionModel().clearSelection();
        durationMinPicker.getSelectionModel().clearSelection();
        reminderPicker.getSelectionModel().clearSelection();
        errorMessage.setVisible(false);
        // TODO: attachments & participants
    }

    public Date createDurationDate(Date date, String durationH, String durationMin) {
        // convert to milliseconds
        long dateMs = date.getTime();
        long hoursMs = (long) Integer.parseInt(durationH) * 60 * 60 * 1000;
        long minsMs = (long) Integer.parseInt(durationMin) * 60 * 1000;

        long durationDateMs = dateMs + hoursMs + minsMs;

        // convert to date
        return new Date(durationDateMs);
    }

    public Date createDateFromInput(LocalDate localDate, String timeString) {
        // extract hours and minutes from timeString
        String[] timeSegments = timeString.split(":");
        int hours = extractTime(timeSegments[0]);
        int mins = extractTime(timeSegments[1]);

        // convert LocalDate to Date
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        // convert everything to milliseconds
        long hoursMs = (long) hours * 60 * 60 * 1000;
        long minsMs = (long) mins * 60 * 1000;
        long dateMs = date.getTime();

        // add hours and minutes to date
        long newDateMs = dateMs + hoursMs + minsMs;

        // convert back
        Date newDate = new Date(newDateMs);
        return newDate;
    }

    public int extractTime(String timeString) {
        return Character.getNumericValue(timeString.charAt(0)) * 10 + Character.getNumericValue(timeString.charAt(1));
    }

    public Event.priorities convertPriorityToEnum(String priorityString) {
        switch (priorityString) {
            case "Low" -> { return Event.priorities.GREEN; }
            case "Medium" -> { return Event.priorities.YELLOW; }
            case "High" -> { return Event.priorities.RED; }
        }
        return null;
    }

    @FXML
    public void onCancel() {
        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        homeView.getMainGrid().getChildren().remove(this);

        resetInputFields();
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
}
