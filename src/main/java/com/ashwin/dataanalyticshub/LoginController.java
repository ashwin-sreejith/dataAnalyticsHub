package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

// Controller for Login and Signup
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

    // Switch views based on login or signup
    private void loadFXML(String fxmlPath) {
        try {

            float width = 800;
            float height = 800;

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            if (Objects.equals(fxmlPath, "Dashboard-view.fxml")){
                DashboardController controller = loader.getController();
                controller.setWelcomeMessage(String.valueOf(userNameField.getText()));
                width = 1200;
                height = 900;
            }
            Scene scene = new Scene(root, width, height);
            System.out.println(userNameField.getText());
            Stage stage = (Stage) userNameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Data Analytics Hub");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // authenticates and logs user in
    public void handleLogin() {

        String username = userNameField.getText().strip().toLowerCase();
        String password = passwordField.getText().strip();

        if (username.isEmpty() || password.isEmpty()) {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Invalid username/password");
            return;
        }

        boolean isAuthenticated = DatabaseHandler.authenticateUser(username, password);

        if (isAuthenticated) {
            switchDashboard();
        } else {
            loginStatusLabel.setTextFill(Color.RED);
            loginStatusLabel.setText("Invalid username/password");
        }
    }

    // handles signup
    public void handleSignup() {

        String firstName = firstNameField.getText().strip().toLowerCase();
        String lastName = lastNameField.getText().strip().toLowerCase();
        String username = userNameField.getText().strip().toLowerCase();
        String password = passwordField.getText();

        // validation for signup fields
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
