package com.example.timescheduler.view;

import com.example.timescheduler.view.components.EventComponent;
import com.example.timescheduler.view.components.EventDetailsComponent;
import com.example.timescheduler.view.components.EventEditComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeView {

    public EventDetailsComponent eventDetails;
    public EventEditComponent eventEdit;

    @FXML
    public GridPane mainGrid;
    @FXML
    ScrollPane eventSection;
    @FXML
    Button adminButton;

    @FXML
    public void initialize() {
        VBox vbox = new VBox();
        vbox.setFillWidth(true);

        for (int i = 0; i<10; i++){
            EventComponent event = new EventComponent();
            vbox.getChildren().add(event);
            VBox.setMargin(event, new Insets(10, 15, 0, 15));
        }

        eventSection.setContent(vbox);
        eventSection.setFitToWidth(true);

        eventEdit = new EventEditComponent(this);

        eventDetails = new EventDetailsComponent(this);
        mainGrid.add(eventDetails, 3, 0);
    }

    @FXML
    protected void onLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }

    @FXML
    protected void onAdminButton(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.adminScene);
    }
}
