package com.example.timescheduler.View;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SchedulerApplication extends Application {
    // for debugging only
    Boolean gridLinesVisible = false;

    public static void main(String[] args) { launch(args); }

    public void start(Stage stage)
    {
        stage.setTitle("Time Scheduler");
        stage.show();

        GridPane grid = createLoginGrid();

        StackPane debugBar = createDebugBar(grid);

        BorderPane debugLayout = new BorderPane();
        debugLayout.setCenter(grid);
        debugLayout.setTop(debugBar);

        Scene scene = new Scene(debugLayout, 400, 400);
        scene.setFill(Color.WHITE);
        // TODO
        // scene.getStylesheets().add(getClass().getResource("loginStyles.css").toExternalForm());

        stage.setScene(scene);
    }

    private StackPane createDebugBar(GridPane grid) {
        StackPane debugBar = new StackPane();

        Button gridDebugBtn = new Button("Show Grid (debug)");
        gridDebugBtn.setId("debug-button");
        gridDebugBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (gridLinesVisible) {
                    grid.setGridLinesVisible(false);
                    gridLinesVisible = false;
                } else {
                    grid.setGridLinesVisible(true);
                    gridLinesVisible = true;
                }
            }
        });

        debugBar.setBackground(new Background(new BackgroundFill(Color.web("hsla(0, 0%, 0%, 0.1)"), CornerRadii.EMPTY, Insets.EMPTY)));
        debugBar.setMinHeight(30);
        debugBar.getChildren().add(gridDebugBtn);

        return debugBar;
    }


    private GridPane createLoginGrid()
    {
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setPadding(new Insets(30, 0, 0, 0));
        grid.setVgap(20);
        grid.setHgap(10);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setHalignment(HPos.CENTER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().addAll(col0, col1);


        Text title = new Text("Login");
        title.setId("login-title");
        // i: column    i1: row     i2: column span     i3: row span
        grid.add(title, 0, 0);
        // insets: top, left, bottom, right
        GridPane.setMargin(title, new Insets(0, 0, 10, 0));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        usernameField.setMinHeight(35);
        grid.add(usernameField, 0, 1);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMinHeight(35);
        grid.add(passwordField, 0, 2);

        // TODO add password visibility toggle in column 1, row 2

        Button loginBtn = new Button("Log In");
        loginBtn.setMinHeight(32);
        loginBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.add(loginBtn, 0, 3);
        GridPane.setFillWidth(loginBtn, true);

        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // TODO notify listener
                System.out.println("Log in button pressed.");
            }
        });

        Separator separator = new Separator();
        grid.add(separator, 0, 4);
        GridPane.setMargin(separator, new Insets(10, 0, 10, 0));

        Hyperlink signUpLink = new Hyperlink("Sign up");
        // TODO add onAction function
        TextFlow signUpFlow = new TextFlow(
                new Text("Don't have an account yet? "), signUpLink
        );
        grid.add(signUpFlow, 0, 5);

        return grid;
    }
}
