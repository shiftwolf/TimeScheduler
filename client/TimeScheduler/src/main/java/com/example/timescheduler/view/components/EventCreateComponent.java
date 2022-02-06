package com.example.timescheduler.view.components;

import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class EventCreateComponent extends GridPane {

    HomeView homeView;

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
    public void onSave() {
        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 3, 0);
        homeView.getMainGrid().getChildren().remove(this);
        homeView.setIsEdit(false);
        System.out.println("Create " + homeView.getIsEdit());
    }

    @FXML
    public void onCancel() {
        homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 3, 0);
        homeView.getMainGrid().getChildren().remove(this);
        homeView.setIsEdit(false);
        System.out.println("Create " + homeView.getIsEdit());
    }

}
