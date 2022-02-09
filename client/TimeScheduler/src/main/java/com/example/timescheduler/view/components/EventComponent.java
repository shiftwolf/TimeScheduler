package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class EventComponent extends HBox {

    HomeView homeView;
    Event event;

    @FXML
    Label name;
    @FXML
    Label details;
    @FXML
    Button detailsButton;
    
    public EventComponent(HomeView homeView, Event event) {
        this.homeView = homeView;
        this.event = event;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event_component.fxml"));
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
        // TODO

        // set values
//        name.setText(event.getName());
//        System.out.println("event date " + event.getDate());
    }

    @FXML
    public void onDetailsButton() {
        // TODO
    }

}