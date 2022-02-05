package com.example.timescheduler.view;

import com.example.timescheduler.view.components.UserComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminView {

    @FXML
    GridPane mainGrid;
    @FXML
    ScrollPane userSection;
    @FXML
    Button adminButton;

    @FXML
    public void initialize() {
        VBox vbox = new VBox();
        vbox.setFillWidth(true);

        for (int i = 0; i<10; i++){
            UserComponent user = new UserComponent();
            vbox.getChildren().add(user);
            VBox.setMargin(user, new Insets(10, 15, 0, 15));
        }

        userSection.setContent(vbox);
        userSection.setFitToWidth(true);

    }

    @FXML
    protected void onLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }

    @FXML
    protected void onAdminButton(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.homeScene);
    }

}
