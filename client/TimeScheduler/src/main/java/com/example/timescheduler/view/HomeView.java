package com.example.timescheduler.view;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.example.timescheduler.Presenter.HomeViewListener;
import com.example.timescheduler.view.components.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeView {

    private static final ArrayList<HomeViewListener> listeners = new ArrayList<>();

    private List<User> users = new ArrayList<>();
    private User selectedUser;
    private List<Event> events = new ArrayList<>();
    private Event selectedEvent;

    private EventDetailsComponent eventDetails;
    private EventEditComponent eventEdit;
    private EventCreateComponent eventCreate;
    private UserEditComponent userEdit;

    private VBox usersSection;
    private VBox eventsSection = new VBox();

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

    @FXML
    public void initialize() {
        // TODO: check if user is admin
        // TODO: disable button if not admin

        // set mainGrid column ratio (1:1)
        gridCol0 = new ColumnConstraints();
        gridCol0.setPercentWidth(50);
        gridCol1 = new ColumnConstraints();
        gridCol1.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(gridCol0, gridCol1);

        // notify listener: getEvents
        notifyOnGetEvents();

        // add events to events section
        for (Event event : events) {
            EventComponent eventComponent = new EventComponent(this, event);
            eventsSection.getChildren().add(eventComponent);
            VBox.setMargin(eventComponent, new Insets(10, 15, 0, 15));
        }

        // initialize the events panel
        eventsSection.setFillWidth(true);

        scrollPane.setContent(eventsSection);
        scrollPane.setFitToWidth(true);

        // initialize the components that make up the right half of the events panel
        eventEdit = new EventEditComponent(this);
        eventDetails = new EventDetailsComponent(this);
        eventCreate = new EventCreateComponent(this);

        // display the default component for this part of the events panel
        mainGrid.add(eventDetails, 1, 0);


        // initialize the users section of the admin panel in order to add items later
        usersSection = new VBox();

        switchToEventsButton = new Button("Events Panel");
        switchToEventsButton.setMinHeight(35);
        switchToEventsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                showEventsPanel();
            }
        });

        userEdit = new UserEditComponent(this);
    }

    @FXML
    protected void onAddEvent(ActionEvent event) {
        // check which component is currently visible in order to remove the correct component
        if (isInGrid(eventEdit)) {
            mainGrid.getChildren().remove(eventEdit);
        } else {
            mainGrid.getChildren().remove(eventDetails);
        }

        // display the create options
        if (!isInGrid(eventCreate)) {
            mainGrid.add(eventCreate, 1, 0);
        }
    }

    @FXML
    protected void onLogout(ActionEvent event) throws IOException {
        // TODO: delete token, clear

        // new window for login
        Parent fxml = FXMLLoader.load(getClass().getResource("login_view.fxml"));
        Scene scene = new Scene(fxml);
        Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        loginStage.setScene(scene);
        loginStage.setWidth(450);
        loginStage.setHeight(380);
        loginStage.setX(200);
        loginStage.setY(100);
        loginStage.show();
        loginStage.centerOnScreen();
    }

    @FXML
    protected void onSwitchToAdminButton(ActionEvent event) {
        // notify listeners
        notifyOnSwitchToAdminPanel();

        // configure GUI
        showAdminPanel();
    }

    private void showAdminPanel() {
        panelTitle.setText("Users");

        // replace buttons
        topBar.getChildren().remove(switchToAdminButton);
        topBar.getChildren().add(2, switchToEventsButton);

        // clear usersSection
        usersSection.getChildren().clear();

        // add users to usersSection
        for (User user : users) {
            UserComponent userComponent = new UserComponent(this, user);
            usersSection.getChildren().add(userComponent);
            VBox.setMargin(userComponent, new Insets(10, 15, 0, 15));
        }

        // configure left half
        // 1. replace events section with users section
        scrollPane.setContent(usersSection);

        System.out.println("scrollpane 1: " + scrollPane);

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

    public void showEventsPanel() {
        // configure top bar:
        panelTitle.setText("Upcoming Events");
        // replace buttons
        topBar.getChildren().remove(switchToEventsButton);
        topBar.getChildren().add(2, switchToAdminButton);

        // clear eventsSection
        eventsSection.getChildren().clear();

        // load events
        notifyOnGetEvents();

        // add events to eventsSection
        for (Event event : events) {
            EventComponent eventComponent = new EventComponent(this, event);
            eventsSection.getChildren().add(eventComponent);
            VBox.setMargin(eventComponent, new Insets(10, 15, 0, 15));
        }
        eventsSection.setFillWidth(true);

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

    public void notifyOnGetEvents() {
        for (HomeViewListener listener : listeners) {
            try {
                events = listener.getEvents(SchedulerApplication.token);
            } catch (Exception e) {
                System.out.println("Requesting events failed.");
            }
        }
    }

    public void notifyOnSwitchToAdminPanel() {
        for (final HomeViewListener listener : listeners) {
            try {
                users = listener.getUsers(SchedulerApplication.token);
            } catch (Exception e) {
                System.out.println("Requesting users failed.");
            }
        }
    }

    public void notifyOnEditUser(String newUsername, String newName, String newEmail) {
        for (HomeViewListener listener : listeners) {
            try {
                listener.editUser(
                        SchedulerApplication.token,
                        selectedUser,
                        newUsername,
                        newName,
                        newEmail);
            } catch (Exception e) {
                System.out.println("Edit user failed.");
            }
        }
    }

    public void notifyOnDeleteUser(User user) {
        for (HomeViewListener listener : listeners) {
            try {
                listener.deleteUser(user);
            } catch (Exception e) {
                System.out.println("Delete user failed.");
            }
        }
    }

    public void addListener(final HomeViewListener listener) { listeners.add(listener); }

    public void initializeDropDownMenus(ComboBox timePicker, ComboBox durationHPicker, ComboBox durationMinPicker) {
        // set time values from 00:00 to 23:55
        for (int h = 0; h < 24; h++) {
            for (int min = 0; min < 60; min += 5) {
                timePicker.getItems().add(formatTime(h, min));
            }
        }
        // set hour values of the duration picker
        for (int h = 0; h < 24; h++) {
            durationHPicker.getItems().add(String.valueOf(h));
        }
        // set minutes values of the duration picker
        for (int min = 5; min < 60; min += 5) {
            durationMinPicker.getItems().add(String.valueOf(min));
        }
    }

    public String formatTime(int h, int m) {
        String hh;
        String mm;

        if (h <= 9) {
            hh = String.format("0%d", h);
        } else {
            hh = String.valueOf(h);
        }

        if (m <= 9) {
            mm = String.format("0%d", m);
        } else {
            mm = String.valueOf(m);
        }

        return String.format("%s:%s", hh, mm);
    }

    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EE  dd.MM.yyyy HH:mm");
        return format.format(date);
    }

    public String formatDuration(Event event) {
        long milliseconds = event.getDuration().getTime();
        // millisec -> hours
        long hours = (((milliseconds/1000)/60)/60);
        // millisec -> minutes
        long minutes = (((milliseconds/1000)/60)%60);

        String durationString = "";
        if (hours > 0) {
            durationString = String.format("%dh ", hours);
        }
        durationString = durationString + String.format("%dmin", minutes);

        return durationString;
    }

    // Getters & Setters

    public EventDetailsComponent getEventDetailsComponent() { return eventDetails; }

    public EventEditComponent getEventEditComponent() { return eventEdit; }

    public EventCreateComponent getEventCreateComponent() { return eventCreate; }

    public GridPane getMainGrid() { return mainGrid; }

    public UserEditComponent getUserEdit() { return userEdit; }

    public void setSelectedUser(User selectedUser) { this.selectedUser = selectedUser; }

    public User getSelectedUser() { return selectedUser; }

    public Event getSelectedEvent() { return selectedEvent; }

    public void setSelectedEvent(Event selectedEvent) { this.selectedEvent = selectedEvent; }
}
