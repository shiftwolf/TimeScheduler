package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import com.example.timescheduler.view.SchedulerApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EventCreateComponent extends GridPane {

    HomeView homeView;

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
    VBox participantsSection;
    @FXML
    TextField newParticipantField;
    @FXML
    VBox attachmentsSection;

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
        SchedulerApplication.initializeDropDownMenus(timePicker, durationHPicker, durationMinPicker);
    }

    @FXML
    public void onSave() {
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
