package com.example.timescheduler.view;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.Presenter.HomeViewListener;
import com.example.timescheduler.view.components.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HomeView {

    private static List<User> users = new ArrayList<>();
    private static final ArrayList<HomeViewListener> listeners = new ArrayList<>();

    private EventDetailsComponent eventDetails;
    private EventEditComponent eventEdit;
    private EventCreateComponent eventCreate;
    private UserEditComponent userEdit;

    private VBox usersSection;
    private VBox eventsSection;

    Button switchToEventsButton;
    ColumnConstraints gridCol0;
    ColumnConstraints gridCol1;

    @FXML
    HBox topBar;
    @FXML
    Label panelTitle;
    @FXML
    GridPane mainGrid;
    @FXML
    ScrollPane scrollPane;
    @FXML
    HBox eventActions;
    @FXML
    Button switchToAdminButton;
    // TODO: disable button if not admin

    public HomeView() {
        // TODO: check if user is admin?

        // initialize the users section of the admin panel in order to add items later
        usersSection = new VBox();

        switchToEventsButton = new Button("Events Panel");
        switchToEventsButton.setMinHeight(35);
        switchToEventsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                // TODO: request events again?
                showEventsPanel();
            }
        });

        userEdit = new UserEditComponent(this);
    }

    @FXML
    public void initialize() {
        // set mainGrid column ratio (1:1)
        gridCol0 = new ColumnConstraints();
        gridCol0.setPercentWidth(50);
        gridCol1 = new ColumnConstraints();
        gridCol1.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(gridCol0, gridCol1);

        // initialize the events panel
        eventsSection = new VBox();
        eventsSection.setFillWidth(true);

        // add the events TODO
        for (int i = 0; i < 10; i++){
            EventComponent event = new EventComponent();
            eventsSection.getChildren().add(event);
            VBox.setMargin(event, new Insets(10, 15, 0, 15));
        }

        // display the events in the ScrollPane
        scrollPane.setContent(eventsSection);
        scrollPane.setFitToWidth(true);

        // initialize the components that make up the right half of the events panel
        eventEdit = new EventEditComponent(this);
        eventDetails = new EventDetailsComponent(this);
        eventCreate = new EventCreateComponent(this);

        // display the default component for this part of the events panel
        mainGrid.add(eventDetails, 1, 0);
    }

    @FXML
    protected void onAddEvent(ActionEvent event) {
        // check which component is currently visible in order to remove the correct component
        if (isInGrid(eventEdit)) {
            mainGrid.getChildren().remove(eventEdit);
        } else {
            mainGrid.getChildren().remove(eventDetails);
        }
        if (!isInGrid(eventCreate)) {
            mainGrid.add(eventCreate, 1, 0);
        }
    }

    @FXML
    protected void onLogout(ActionEvent event) {
        // TODO: delete token

        // navigate to Login
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }

    @FXML
    protected void onSwitchToAdminButton(ActionEvent event) {
        // notify listeners: request users
        for (final HomeViewListener listener : listeners) {
            try {
                users = listener.getUsers(SchedulerApplication.token);
            } catch (Exception e) {
                System.out.println("Requesting users failed.");
            }
        }

        showAdminPanel();
    }

    private void showAdminPanel() {
        // configure top bar:
        panelTitle.setText("Users");
        // replace buttons
        topBar.getChildren().remove(switchToAdminButton);
        topBar.getChildren().add(2, switchToEventsButton);

        // add users to usersSection
        for (User user : users) {
            UserComponent userComponent = new UserComponent(this);
            usersSection.getChildren().add(userComponent);
            VBox.setMargin(userComponent, new Insets(10, 15, 0, 15));
        }

        // configure left half
        // 1. replace events section with users section
        scrollPane.setContent(usersSection);
        // 2. configure ratio of the ScrollPane (7:3)
        gridCol0.setPercentWidth(70);
        gridCol1.setPercentWidth(30);
        // 3. remove the buttons on the left
        eventActions.setVisible(false);

        // configure right half: check which component is currently visible in order to remove the correct component
        if (isInGrid(eventEdit)) {
            mainGrid.getChildren().remove(eventEdit);
        } else if (isInGrid(eventCreate)) {
            mainGrid.getChildren().remove(eventCreate);
        } else {
            mainGrid.getChildren().remove(eventDetails);
        }
    }

    private void showEventsPanel() {
        // configure top bar:
        panelTitle.setText("Upcoming Events");
        // replace buttons
        topBar.getChildren().remove(switchToEventsButton);
        topBar.getChildren().add(2, switchToAdminButton);

        // add events to eventsSection TODO: use real data
        for (int i = 0; i < 10; i++){
            EventComponent eventComponent = new EventComponent();
            eventsSection.getChildren().add(eventComponent);
            VBox.setMargin(eventComponent, new Insets(10, 15, 0, 15));
        }

        // configure left half
        // 1. replace users section with events section
        scrollPane.setContent(eventsSection);
        // 2. configure ratio of the ScrollPane (1:1)
        gridCol0.setPercentWidth(50);
        gridCol1.setPercentWidth(50);
        // 3. show the buttons on the left
        eventActions.setVisible(true);

        // configure right half
        // 1. remove EditUser
        if (isInGrid(userEdit)) {
            mainGrid.getChildren().remove(userEdit);
        }
        // 2. show eventDetails
         mainGrid.add(eventDetails, 1, 0);
    }

    public boolean isInGrid(Node node) {
        for (Node gridItem : mainGrid.getChildren()) {
            if (node == gridItem) { return true; }
        }
        return false;
    }

    public void addListener(final HomeViewListener listener) { listeners.add(listener); }

    public EventDetailsComponent getEventDetailsComponent() { return eventDetails; }

    public EventEditComponent getEventEditComponent() { return eventEdit; }

    public GridPane getMainGrid() { return mainGrid; }

    public UserEditComponent getUserEdit() { return userEdit; }
}
