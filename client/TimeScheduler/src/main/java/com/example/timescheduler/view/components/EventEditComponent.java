package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.view.HomeView;
import com.example.timescheduler.view.SchedulerApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class is the controller of the event edit component.
 * It handles all user interactions with the input controls that enable the user to modify the event.
 */
public class EventEditComponent extends GridPane {

    HomeView homeView;

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
    Label errorMessage;


    public EventEditComponent(HomeView homeView) {
        this.homeView = homeView;

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
        // TODO: keep track of selected event

        // reset error message
        errorMessage.setVisible(false);

        boolean isValid = checkIfInputValid();

        System.out.println(isValid);

        if (isValid) {
            // get entered name & location
            String name = nameField.getText().trim();
            String location = locationField.getText().trim();
            // convert input to priorities enum
            Event.priorities priority = convertPriorityToEnum(priorityPicker.getValue().toString());
            // create date
            Date date = createDateFromInput(datePicker.getValue(), timePicker.getValue().toString());
            // create duration date
            Date duration = createDurationDate(
                    durationHPicker.getValue().toString(),
                    durationMinPicker.getValue().toString());
            // create reminder date
            Date reminder = createReminderDate(date, reminderPicker.getValue().toString());

            homeView.notifyOnEditEvent(
                    homeView.getSelectedEvent().getId(),
                    name,
                    date,
                    duration,
                    location,
                    priority,
                    reminder);

            // update displayed events
            homeView.updateEvents();

            // update GUI
            if (!homeView.isInGrid(homeView.getEventDetailsComponent())) {
                homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
            }
            homeView.getMainGrid().getChildren().remove(this);

            resetInputFields();
        } else {
            errorMessage.setVisible(true);
        }

        if (!homeView.isInGrid(homeView.getEventDetailsComponent())) {
            homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        }
        homeView.getMainGrid().getChildren().remove(this);
    }

    @FXML
    public void onCancel() {
        // TODO: keep track of selected event


        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        homeView.getMainGrid().getChildren().remove(this);
    }

    public void setInitialValues(Event event) {
        nameField.setText(event.getName());

        priorityPicker.setValue(convertToString(event.getPriority()));

        datePicker.setValue(convertToLocalDate(event.getDate()));

        timePicker.setValue(getTimeFormatted(event.getDate()));

        // convert the duration to the right format before setting the values
        String durationString = formatDurationNumbersOnly(event);
        String[] durationSegments = durationString.split(" ");
        durationHPicker.setValue(durationSegments[0]);
        durationMinPicker.setValue(durationSegments[1]);

        reminderPicker.setValue(createReminderString(event));

        locationField.setText(event.getLocation());
    }

    public String createReminderString(Event event) {
        long reminderMs = event.getDate().getTime() - event.getReminder().getTime();

        // 1 week -> ms
        long weekMs = 7 * 24 * 60 * 60 * 1000;
        // 3 days -> ms
        long daysMs = 3 * 24 * 60 * 60 * 1000;
        // 1 hour -> ms
        long hourMs = 60 * 60 * 1000;
        // 10 min -> ms
        long minMs = 10 * 60 * 1000;

        // convert reminderMs to string
        String reminderString = "";

        if (reminderMs == weekMs){
            reminderString = "1 week";
        } else if (reminderMs == daysMs) {
            reminderString = "3 days";
        } else if (reminderMs == hourMs) {
            reminderString = "1 hour";
        } else if (reminderMs == minMs) {
            reminderString = "10 min";
        }
        return reminderString;
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
            durationString = "0 ";
        }
        if (minutes > 0){
            durationString = durationString + String.format("%d", minutes);
        } else {
            durationString = durationString + "0";
        }

        return durationString;
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

    public Date createDurationDate(String durationH, String durationMin) {
        // convert to milliseconds
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

}
