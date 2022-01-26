package com.example.timescheduler.View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class HomeView {
    @FXML
    ScrollPane eventSection;

    @FXML
    protected void onLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }
}
