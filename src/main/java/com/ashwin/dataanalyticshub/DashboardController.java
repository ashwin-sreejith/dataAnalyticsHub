package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableView;

public class DashboardController {
    @FXML
    public ListView postOperations;
    @FXML
    private Label userLabel;

    @FXML
    private MenuButton optionsMenu;

    @FXML
    private TableView<?> postTableView;

    private String userName;

    // Method to set the welcome message with the provided username
    public void setWelcomeMessage(String username) {
        this.userName = username;
        String fullName = DatabaseHandler.getFullNameByUsername(this.userName);
        userLabel.setText("Welcome, " + fullName +"!");
    }

    // Initialize the dashboard components
    public void initialize() {
        // TODO: Initialize and populate the TableView with user posts
    }
}
