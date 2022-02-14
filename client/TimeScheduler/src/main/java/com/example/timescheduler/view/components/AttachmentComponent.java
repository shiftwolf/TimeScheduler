package com.example.timescheduler.view.components;

import com.example.timescheduler.Model.AttachmentsInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class controls the attachment component. It displays the most important attachment information and
 * handles user interactions.
 */
public class AttachmentComponent extends HBox {

    EventDetailsComponent eventDetailsComponent;
    AttachmentsInfo attachment;

    @FXML
    Label fileName;

    public AttachmentComponent(EventDetailsComponent eventDetailsComponent, AttachmentsInfo attachment) {
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
        fileName.setText(attachment.getFilename());
    }

    @FXML
    public void onDownload(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        byte[] bytes = eventDetailsComponent.homeView.notifyOnDownloadAtt(attachment.getId());

        File file = fileChooser.showSaveDialog((Stage) ((Node)actionEvent.getSource()).getScene().getWindow());
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            String path = file.getAbsolutePath();

            try (FileOutputStream stream = new FileOutputStream(path)) {
                stream.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onRemove() {
        // TODO

    }
}
