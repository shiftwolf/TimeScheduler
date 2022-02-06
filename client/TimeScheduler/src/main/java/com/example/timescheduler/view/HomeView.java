package com.example.timescheduler.view;

import com.example.timescheduler.view.components.EventComponent;
import com.example.timescheduler.view.components.EventCreateComponent;
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

    private EventDetailsComponent eventDetails;
    private EventEditComponent eventEdit;
    private EventCreateComponent eventCreate;
    private boolean isEdit = false;

    @FXML
    GridPane mainGrid;
    @FXML
    ScrollPane eventSection;
    @FXML
    Button adminButton;

    @FXML
    public void initialize() {
        VBox vbox = new VBox();
        vbox.setFillWidth(true);

        for (int i = 0; i < 10; i++){
            EventComponent event = new EventComponent();
            vbox.getChildren().add(event);
            VBox.setMargin(event, new Insets(10, 15, 0, 15));
        }

        eventSection.setContent(vbox);
        eventSection.setFitToWidth(true);

        eventEdit = new EventEditComponent(this);
        eventDetails = new EventDetailsComponent(this);
        eventCreate = new EventCreateComponent(this);

        mainGrid.add(eventDetails, 3, 0);
    }

    @FXML
    protected void onAddEvent(ActionEvent event) {
        if (isEdit) {
            mainGrid.getChildren().remove(eventEdit);
        } else {
            mainGrid.getChildren().remove(eventDetails);
        }
        if (!isInGrid(eventCreate)) {
            mainGrid.add(eventCreate, 3, 0);
        }
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

    boolean isInGrid(Node node) {
        for (Node gridItem : mainGrid.getChildren()) {
            if (node == gridItem) { return true; }
        }
        return false;
    }

    public EventDetailsComponent getEventDetailsComponent() { return eventDetails; }

    public EventEditComponent getEventEditComponent() { return eventEdit; }

    public GridPane getMainGrid() { return mainGrid; }

    public boolean getIsEdit() { return isEdit; }

    public void setIsEdit(boolean val) { isEdit = val; }
}
