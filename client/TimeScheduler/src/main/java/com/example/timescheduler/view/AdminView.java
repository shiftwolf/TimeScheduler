package com.example.timescheduler.view;

import com.example.timescheduler.Model.User;
import com.example.timescheduler.Presenter.HomeViewListener;
import com.example.timescheduler.view.components.UserComponent;
import com.example.timescheduler.view.components.UserEditComponent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class AdminView {
//    private static List<User> users = new ArrayList<>();
//    private static final ArrayList<HomeViewListener> listeners = new ArrayList<>();

    boolean isEditVisible = false;
    UserEditComponent userEdit;

    @FXML
    GridPane mainGrid;
    @FXML
    ScrollPane userScrollPane;
    @FXML
    VBox userSection;

    public AdminView() {
        System.out.println("- AdminView constructor - ");
        // notify listeners: request users

//        for (final AdminViewListener listener : listeners) {
//            try {
//                System.out.println("AdminView users output: " + listener.getUsers(SchedulerApplication.token));
//            } catch (Exception e) {
//                System.out.println("Error");
//            }
//        }

    }

    @FXML
    public void initialize() {
//        userEdit = new UserEditComponent(this);
//
//        userSection.setFillWidth(true);
//
//        for (int i = 0; i<10; i++){
//            UserComponent user = new UserComponent(this);
//            userSection.getChildren().add(user);
//            VBox.setMargin(user, new Insets(10, 15, 0, 15));
//        }
//
//        userScrollPane.setContent(userSection);
//        userScrollPane.setFitToWidth(true);
    }

    @FXML
    protected void onLogout(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.loginScene);
    }

    @FXML
    protected void onAdminButton(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(SchedulerApplication.homeScene);
    }

//    public void addListener(final HomeViewListener listener) { listeners.add(listener); }

    public GridPane getMainGrid() { return mainGrid; }

    public UserEditComponent getUserEdit() { return userEdit; }

    public boolean getIsEditVisible() { return isEditVisible; }

    public void setIsEditVisible(boolean val) { isEditVisible = val; }
}
