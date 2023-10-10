package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class LoginController {
    @FXML
    public Label loginStatusLabel;
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    private void loadFXML(String fxmlPath) {
        try {

            float width = 800;
            float height = 800;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (Objects.equals(fxmlPath, "Dashboard-view.fxml")){
                DashboardController controller = loader.getController();
                controller.setWelcomeMessage(String.valueOf(userNameField.getText()));
                width = 1800;
                height = 1200;
            }
            Scene scene = new Scene(root, width, height);
            Stage stage = (Stage) ((Node) userNameField).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Data Analytics Hub");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLogin() {

        String username = userNameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Invalid username/password");
            return;
        }

        boolean isAuthenticated = DatabaseHandler.authenticateUser(username, password);

        if (isAuthenticated) {
//            loginStatusLabel.setTextFill(Color.GREEN);
//            loginStatusLabel.setText("Login successful!");
            switchDashboard();
        } else {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Invalid username/password");
        }
    }

    public void handleSignup() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = userNameField.getText();
        String password = passwordField.getText();

        if (firstName.isBlank()) {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("First Name cannot be empty");
        } else if (lastName.isBlank()) {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Last Name cannot be empty");
        } else if (username.isBlank()) {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Username cannot be empty");
        } else if (password.isBlank()) {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Password cannot be empty");
        } else {
            int errorCode = DatabaseHandler.insertUser(firstName, lastName, username, password);
            loginStatusLabel.setTextFill(Color.RED);
            if (errorCode == 19) {
                loginStatusLabel.setText("Username already in use");
            } else if (errorCode == -1) {
                loginStatusLabel.setText("Something went wrong!");
            } else {
                loginStatusLabel.setTextFill(Color.GREEN);
                loginStatusLabel.setText("Successful. Please Login");
            }
        }


    }


    public void switchSignup() {
        //Load the SignupView.fxml
        loadFXML("Signup-view.fxml");
    }

    public void switchLogin() {
        // Load the LoginView.fxml
        loadFXML("Login-view.fxml");
    }

    public void switchDashboard() {
        // Load the LoginView.fxml
        loadFXML("Dashboard-view.fxml");
    }

}
