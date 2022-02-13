package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.view.HomeView;
import com.example.timescheduler.view.SchedulerApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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
            // create reminder date
            Date reminder = createReminderDate(date, reminderPicker.getValue().toString());

            System.out.println(
                    "date: " + date + "\nduration date: " + duration + "\nreminder date: " + reminder);
            System.out.println(
                    "date ms: " + date.getTime() + "\nduration date ms: " + duration.getTime() + "\nreminder date ms: " + reminder.getTime());

            homeView.notifyOnCreateEvent(
                    name,
                    date,
                    duration,
                    location,
                    priority,
                    new String[0],
                    reminder,
                    SchedulerApplication.token);

            // TODO: update events list
            homeView.updateEvents();

            // update GUI
            homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
            homeView.getMainGrid().getChildren().remove(this);

            resetInputFields();
        } else {
            errorMessage.setVisible(true);
        }
    }

    public Date createReminderDate(Date eventDate, String reminderValue) {
        // 1 week -> ms
        long weekMs = 7 * 24 * 60 * 60 * 1000;
        // 3 days -> ms
        long daysMs = 3 * 24 * 60 * 60 * 1000;
        // 1 hour -> ms
        long hourMs = 60 * 60 * 1000;
        // 10 min -> ms
        long minMs = 10 * 60 * 1000;

        long dateMs = eventDate.getTime();

        // convert reminderValue to ms
        long reminderMs = 0;
        switch (reminderValue) {
            case "1 week" -> { reminderMs = weekMs; }
            case "3 days" -> { reminderMs = dateMs; }
            case "1 hour" -> { reminderMs = hourMs; }
            case "10 min" -> { reminderMs = minMs; }
        }

        // difference
        long reminderDateMs = dateMs - reminderMs;

        // convert to date
        System.out.println("reminder date: " + new Date(reminderDateMs));
        return new Date(reminderDateMs);
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
    }

    public Date createDurationDate(Date date, String durationH, String durationMin) {
        // convert to milliseconds
//        long dateMs = date.getTime();
        long hoursMs = (long) Integer.parseInt(durationH) * 60 * 60 * 1000;
        long minsMs = (long) Integer.parseInt(durationMin) * 60 * 1000;

        long durationDateMs = hoursMs + minsMs;

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
        System.out.println("date from input: " + newDate);
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
}
