package com.example.timescheduler.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class HomeView {
    @FXML ScrollPane eventSection;

    @FXML
    public void initialize() {
//        VBox vbox = new VBox();
//        for (int i = 0; i<5; i++){
//            vbox.getChildren().add(new EventComponent());
//        }
//        vbox.setSpacing(20);
//        eventSection.setContent(vbox);
    }

    @FXML
    protected void onLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }
}
