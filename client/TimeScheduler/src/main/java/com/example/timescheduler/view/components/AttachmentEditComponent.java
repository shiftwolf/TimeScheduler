package com.example.timescheduler.view.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class AttachmentEditComponent extends HBox {

    EventEditComponent eventEditComponent;
    String attachment;

    @FXML
    Label fileName;

    public AttachmentEditComponent(EventEditComponent eventEditComponent, String attachment) {
        this.attachment = attachment;
        this.eventEditComponent = eventEditComponent;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("attachment_edit_component.fxml"));
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
    public void onRemove() {
        // TODO: save change in edit view
        eventEditComponent.getAttachmentsSection().getChildren().remove(this);
    }
}
