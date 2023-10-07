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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 800);

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

        try (Connection connection = DatabaseHandler.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Successful login
                loginStatusLabel.setTextFill(Color.GREEN);
                loginStatusLabel.setText("Login successful!");
            } else {
                // Invalid credentials
                loginStatusLabel.setTextFill(Color.RED);
                loginStatusLabel.setText("Invalid username/password");
            }

        } catch (SQLException e) {
            loginStatusLabel.setText("Error during login: " + e.getMessage());
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
            // All fields are filled, proceed with insertion
            DatabaseHandler.insertUser(firstName, lastName, username, password);
            loginStatusLabel.setTextFill(Color.GREEN);
            loginStatusLabel.setText("Successful. Please Login");
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
}
