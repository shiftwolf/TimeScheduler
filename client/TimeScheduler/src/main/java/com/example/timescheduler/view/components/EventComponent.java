package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.view.HomeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class EventComponent extends HBox {

    HomeView homeView;
    Event event;

    @FXML
    Label nameField;
    @FXML
    Label detailsField;
    
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
        // set name
        nameField.setText(event.getName());
        // format & display the date
        detailsField.setText(homeView.formatDate(event.getDate()));
    }

    @FXML
    public void onDetailsButton() {
        // keep track of the currently selected event
        homeView.setSelectedEvent(event);

        // remove the other components and add the details component
        if (homeView.isInGrid(homeView.getEventEditComponent())) {
            homeView.getMainGrid().getChildren().remove(homeView.getEventEditComponent());
            homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        } else if (homeView.isInGrid(homeView.getEventCreateComponent())) {
            homeView.getMainGrid().getChildren().remove(homeView.getEventCreateComponent());
            homeView.getMainGrid().add(homeView.getEventDetailsComponent(), 1, 0);
        }

        showDetails();
    }

    private void showDetails() {
        // calls a function in the details component to update the displayed information
        homeView.getEventDetailsComponent().setDetails(event);
    }
}