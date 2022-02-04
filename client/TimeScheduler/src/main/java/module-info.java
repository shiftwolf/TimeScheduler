module com.example.timescheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires retrofit2;
    requires retrofit2.converter.gson;

    opens com.example.timescheduler.view to javafx.fxml;
    exports com.example.timescheduler.view;
}