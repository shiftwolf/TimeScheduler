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
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    opens com.example.timescheduler.view to javafx.fxml;
    exports com.example.timescheduler.view;

    opens com.example.timescheduler.Model to com.fasterxml.jackson.databind;
    exports com.example.timescheduler.Model;

    opens com.example.timescheduler.Controller to com.fasterxml.jackson.databind;
    exports com.example.timescheduler.Controller;

    opens com.example.timescheduler.APIobjects to com.fasterxml.jackson.databind;
    exports com.example.timescheduler.APIobjects;
    exports com.example.timescheduler.view.components;
    opens com.example.timescheduler.view.components to javafx.fxml;
}