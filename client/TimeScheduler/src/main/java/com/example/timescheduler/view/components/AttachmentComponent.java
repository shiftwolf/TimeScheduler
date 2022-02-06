package com.example.timescheduler.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AttachmentComponent extends HBox {

    EventDetailsComponent eventDetailsComponent;
    String attachment;

    @FXML
    Label fileName;

    public AttachmentComponent(EventDetailsComponent eventDetailsComponent, String attachment) {
        this.attachment = attachment;
        this.eventDetailsComponent = eventDetailsComponent;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("attachment_component.fxml"));
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
        fileName.setText(attachment);
    }

    @FXML
    public void onDownload() {
        // TODO
    }

}
