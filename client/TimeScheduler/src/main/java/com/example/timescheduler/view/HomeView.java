package com.example.timescheduler.view;

import com.example.timescheduler.APIobjects.token;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is the controller of the home_view.fxml.
 * It handles the user interactions with the home window and loads the required GUI components.
 */
public class HomeView {

    private static final ArrayList<HomeViewListener> listeners = new ArrayList<>();

    private List<User> users = new ArrayList<>();
    private User selectedUser;
    private List<Event> events = new ArrayList<>();
    private Event selectedEvent;
    private User loggedUser;

    private EventDetailsComponent eventDetails;
    private EventEditComponent eventEdit;
    private EventCreateComponent eventCreate;
    private UserEditComponent userEdit;
    private UserComponent selectedUserComponent = null;

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

    public HomeView() {
        loggedUser = notifyOnGetLoggedUser();
        for (HomeViewListener listener : listeners) {
            listener.setUser(loggedUser);
        }
    }

    /**
     * This function configures the layout of the home window when it is created.
     * It requests and loads the events and initializes the admin panel.
     */
    @FXML
    public void initialize() {
        // set mainGrid column ratio (1:1)
        gridCol0 = new ColumnConstraints();
        gridCol0.setPercentWidth(50);
        gridCol1 = new ColumnConstraints();
        gridCol1.setPercentWidth(50);
        mainGrid.getColumnConstraints().addAll(gridCol0, gridCol1);

        // notify listener: getEvents
        notifyOnGetEvents();

        // automatically select first event
        if (!events.isEmpty()) {
            selectedEvent = events.get(0);
            System.out.println("auto select first event: " + selectedEvent);
        } else {
            // if user has no events yet
            System.out.println("auto select first event (no events): " + selectedEvent);
            selectedEvent = null;
        }

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

        // display details of the first event
        eventDetails.setDetails(selectedEvent);

        // initialize the users section of the admin panel in order to add items later
        usersSection = new VBox();

        // create the button that navigates back to the events panel, so it can be displayed later
        switchToEventsButton = new Button("Events Panel");
        switchToEventsButton.setMinHeight(35);
        switchToEventsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                showEventsPanel();
            }
        });

        userEdit = new UserEditComponent(this);

        // only show the switch to admin panel button if the user is admin
        if (loggedUser.isAdmin()) {
            switchToAdminButton.setDisable(false);
            switchToAdminButton.setVisible(true);
        }
    }

    /**
     * This function is called when the add event button is pressed.
     * It loads the create event component in the right half of the events panel.
     */
    @FXML
    protected void onAddEvent() {
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

    /**
     * This function is called when the log out button is pressed.
     * It navigates the user back to the login and registration window.
     * @param event Event that represents the action that the corresponding button has been pressed.
     * @throws IOException Exception that occurs if an error arises in FXMLLoader.load.
     */
    @FXML
    protected void onLogout(ActionEvent event) throws IOException {
        // notify listener: logout
        notifyOnLogout();

        // reset token locally
        SchedulerApplication.token = null;

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

    /**
     * This function is called when the button that loads the admin panel is pressed.
     * When this happens, all user information is requested and the events panel is replaced with the admin panel.
     */
    @FXML
    protected void onSwitchToAdminButton() {
        // notify listeners
        notifyOnGetUsers();

        // configure GUI
        showAdminPanel();
    }

    @FXML
    protected void onExportSchedule(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Weekly_Schedule.pdf");

        byte[] bytes = notifyOnGetSchedule();

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

    /**
     * This function is called after the switch to admin button is pressed and handles replacing the events panel
     * with the admin panel.
     */
    private void showAdminPanel() {
        panelTitle.setText("Admin Panel");

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

    /**
     * This function is called after the button that switches back to the events panel is pressed.
     * It requests to update the events list and handles loading the events panel.
     */
    public void showEventsPanel() {
        // configure top bar:
        panelTitle.setText("Upcoming Events");
        // replace buttons
        topBar.getChildren().remove(switchToEventsButton);
        topBar.getChildren().add(2, switchToAdminButton);

        // load & display the current events
        updateEvents();

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

    public void updateAfterEditUser() {
        notifyOnGetUsers();

        // update whole section (fastest to implement right now):

        // clear usersSection
        usersSection.getChildren().clear();

        // add users to usersSection
        for (User user : users) {
            UserComponent userComponent = new UserComponent(this, user);
            usersSection.getChildren().add(userComponent);
            VBox.setMargin(userComponent, new Insets(10, 15, 0, 15));
        }
    }

    public void deleteSelectedEvent() {
        // notify listener
        notifyOnDeleteEvent();

        // update events to update GUI
        eventsSection.getChildren().clear();
        notifyOnGetEvents();
        // add events to eventsSection
        for (Event event : events) {
            EventComponent eventComponent = new EventComponent(this, event);
            eventsSection.getChildren().add(eventComponent);
            VBox.setMargin(eventComponent, new Insets(10, 15, 0, 15));
        }
    }

    public byte[] notifyOnGetSchedule() {
        byte[] bytes = null;
        for (HomeViewListener listener : listeners) {
            bytes = listener.getSchedule(SchedulerApplication.token);
        }
        return bytes;
    }

    public User notifyOnGetLoggedUser() {
        User user = new User();
        for (HomeViewListener listener : listeners) {
            user = listener.getLoggedUser(SchedulerApplication.token);
        }
        return user;
    }

    /**
     * This function is used by the view to notify the listeners about a specific interaction.
     * In this case, the view notifies the presenter that it needs a list of all events of this user.
     */
    public void notifyOnGetEvents() {
        for (HomeViewListener listener : listeners) {
            try {
                events = listener.getEvents(SchedulerApplication.token);
            } catch (Exception e) {
                System.out.println("Requesting events failed: " + e.getMessage());
            }
        }
    }

    public Event notifyOnGetEventById(long eventId) {
        Event event = null;
        for (HomeViewListener listener : listeners) {
            event = listener.getEventById(SchedulerApplication.token, eventId);
        }
        return event;
    }

    public void notifyOnCreateEvent(String name, Date date, Date duration, String location, Event.priorities priority,
                                    String[] participantMails, Date reminder, token token) {
        for (HomeViewListener listener : listeners) {
            listener.createEvent(name, date, duration, location, priority, participantMails, reminder, token);
        }
    }

    public void notifyOnEditEvent(Long eventId, String name, Date date, Date duration, String location,
                                  Event.priorities priority, Date reminder) {
        for (HomeViewListener listener : listeners) {
            listener.editEvent(eventId, name, date, duration, location, priority, reminder, SchedulerApplication.token);
        }
    }

    public int notifyOnAddParticipant(String email) {
        int response = 3;
        for (HomeViewListener listener : listeners) {
            response = listener.addParticipant(SchedulerApplication.token, email, selectedEvent.getId());
        }
        System.out.println("response: " + response);
        return response;
    }

    public int notifyOnAddAttachment(String path) {
        int response = 2;
        for (HomeViewListener listener : listeners) {
            response = listener.addAttachment(SchedulerApplication.token, selectedEvent.getId(), path);
        }
        return response;
    }

    public void notifyOnDeleteEvent() {
        for (HomeViewListener listener : listeners) {
            listener.deleteEvent(SchedulerApplication.token, selectedEvent);
        }
    }

    /**
     * This function is used by the view to notify the listeners about a specific interaction.
     * In this case, the view notifies the presenter that the admin panel is requested, so it needs a list of all
     * users.
     */
    public void notifyOnGetUsers() {
        for (final HomeViewListener listener : listeners) {
            users = listener.admin_getUsers(SchedulerApplication.token);
        }
    }

    /**
     * This function is used by the view to notify the listeners about a specific interaction.
     * In this case, the view notifies the presenter that the admin has edited a user and wants to save the
     * changes.
     * @param newUsername Updated username if the admin has modified it.
     * @param newName Updated name of the user if the admin has modified it.
     * @param newEmail Updated email address of the user if the admin has modified it.
     */
    public void notifyOnEditUser(String newUsername, String newName, String newEmail) {
        for (HomeViewListener listener : listeners) {
            // TODO: don't need try catch anymore
            try {
                listener.admin_editUser(
                        SchedulerApplication.token,
                        selectedUser,
                        newUsername,
                        newName,
                        newEmail);
            } catch (Exception e) {
                System.out.println("Edit user failed: " + e.getMessage());
            }
        }
    }

    /**
     * This function is used by the view to notify the listeners about a specific interaction.
     * In this case, the view notifies the presenter that the admin wants to delete a user account.
     * @param user User object to be deleted.
     */
    public void notifyOnDeleteUser(User user) {
        for (HomeViewListener listener : listeners) {
            // TODO: don't need try catch anymore
            try {
                listener.admin_deleteUser(SchedulerApplication.token, user);
            } catch (Exception e) {
                System.out.println("Delete user failed: " + e.getMessage());
            }
        }
    }

    public void notifyOnLogout() {
        for (HomeViewListener listener : listeners) {
            listener.logout(SchedulerApplication.token);
        }
    }

    public void updateEvents() {
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
    }

    /**
     * This function adds a listener to the HomeView which will from then on be notified when the user interacts with the GUI.
     * @param listener Listener that implements the interface HomeViewListener (presenter).
     */
    public void addListener(final HomeViewListener listener) { listeners.add(listener); }

    /**
     * This is a helper function that it used frequently to determine if a certain node is currently in the grid.
     * @param node This is the node that should be searched in the grid.
     * @return It returns true if the node is in the grid and false if it is not.
     */
    public boolean isInGrid(Node node) {
        for (Node gridItem : mainGrid.getChildren()) {
            if (node == gridItem) { return true; }
        }
        return false;
    }

    /**
     * This is a helper function that is used to initially set the values of all necessary drop-down menus.
     * @param timePicker ComboBox that enables the user to choose the time of an event.
     * @param durationHPicker ComboBox that enables the user to pick how many hours the event will take.
     * @param durationMinPicker ComboBox that enables the user to pick how many minutes the event will take in addition
     *                          to the hours.
     */
    public void initializeDropDownMenus(ComboBox timePicker, ComboBox durationHPicker, ComboBox durationMinPicker) {
        // set time values from 00:00 to 23:55
        for (int h = 0; h < 24; h++) {
            for (int min = 0; min < 60; min += 5) {
                timePicker.getItems().add(formatTime(h, min));
            }
        }
        // set hour values of the duration picker from 0 to 23
        for (int h = 0; h < 24; h++) {
            durationHPicker.getItems().add(String.valueOf(h));
        }
        // set minutes values of the duration picker from 0 to 55
        for (int min = 0; min < 60; min += 5) {
            durationMinPicker.getItems().add(String.valueOf(min));
        }
    }

    /**
     * This is a helper function that converts a time given as hours and minutes to this format: hh:mm.
     * @param h Hours.
     * @param m Minutes.
     * @return Time in hh:mm format.
     */
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

    /**
     * This is a helper function that converts a date to this format: DayOfTheWeek dd.mm.yyyy hh:mm.
     * @param date Date to be converted.
     * @return Date in 'DayOfTheWeek dd.mm.yyyy hh:mm' format.
     */
    public String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EE  dd.MM.yyyy HH:mm");
        return format.format(date);
    }

    /**
     * This is a helper function that converts the duration of an event to this format: e.g. 1h 30min.
     * @param event Event object that stores the start time of the event and the duration as a Date.
     * @return String in the new format.
     */
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

    public String convertReminderToString(Event event) {
        // 1 week -> ms
        long weekMs = 7 * 24 * 60 * 60 * 1000;
        // 3 days -> ms
        long daysMs = 3 * 24 * 60 * 60 * 1000;
        // 1 hour -> ms
        long hourMs = 60 * 60 * 1000;
        // 10 min -> ms
        long minMs = 10 * 60 * 1000;

        // convert event date to milliseconds
        long eventDateMs = event.getDate().getTime();
        // convert event reminder date to milliseconds
        long reminderDateMs = event.getReminder().getTime();

        long differenceMs = eventDateMs - reminderDateMs;

        if (differenceMs == weekMs) {
            return "1 week";
        } else if (differenceMs == daysMs) {
            return "3 days";
        } else if (differenceMs == hourMs) {
            return "1 hour";
        } else if (differenceMs == minMs) {
            return "10 minutes";
        } else {
            return "undefined";
        }
    }

    public void removeUser() {
        users.remove(selectedUser);
    }

    public void removeUserComponent() {
        usersSection.getChildren().remove(selectedUserComponent);
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

    public void setSelectedUserComponent(UserComponent userComponent) {
        this.selectedUserComponent = userComponent;
    }
}
