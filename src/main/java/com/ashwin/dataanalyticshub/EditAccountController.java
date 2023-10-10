package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class EditAccountController {
    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public Label editStatusLabel;
    @FXML
    public TextField newUserNameField;
    public Label validateStatusLabel;
    public PasswordField newPasswordField;
    public VBox editAccountForm;
    public VBox validateForm;
    public GridPane rootGridpane;
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    private String username;
    private HashMap<String, String> userDetails = new HashMap<>();

    @FXML
    public void initialize() {
        editAccountForm.setVisible(false);
        editAccountForm.setManaged(false);
    }

    public void setUserName(String username) {
        this.username = username;
        fetchCurrentUserDetails(this.username);
    }

    public void fetchCurrentUserDetails(String username) {
        if (username != null && !username.isEmpty()) {
            userDetails = DatabaseHandler.fetchAccountDetails(username);
            firstNameField.setText(userDetails.get("firstname"));
            lastNameField.setText(userDetails.get("lastname"));
        }
    }

    public void handleEdit() throws IOException {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String newUserName = newUserNameField.getText();
        String newPassword = newPasswordField.getText();


        if (firstName.isBlank()) {
            editStatusLabel.setTextFill(Color.RED);
            editStatusLabel.setText("FirstName cannot be empty");
        } else if (lastName.isBlank()) {
            editStatusLabel.setTextFill(Color.RED);
            editStatusLabel.setText("LastName cannot be empty");
        } else if(newUserName.isBlank()) {
            editStatusLabel.setTextFill(Color.RED);
            editStatusLabel.setText("Username cannot be empty");
        }  else if(newPassword.isBlank()) {
            editStatusLabel.setTextFill(Color.RED);
            editStatusLabel.setText("Password cannot be empty");
        } else {
            boolean doesUserExist = false;
            if (!newUserName.equals(this.username)) {
                doesUserExist = DatabaseHandler.doesUsernameExist(newUserName);
            }
            if (doesUserExist) {
                editStatusLabel.setTextFill(Color.RED);
                editStatusLabel.setText("Username already taken!");
                return;
            }

            Boolean updateSuccessful = DatabaseHandler.updateUsernameAndReferences(this.username, newUserName, newPassword,
                    firstName, lastName);

            if (updateSuccessful) {

                editStatusLabel.setTextFill(Color.GREEN);
                editStatusLabel.setText("Update Successful. Login Again!");

            } else {
                editStatusLabel.setText("Something went wrong! Contact admin");
            }



        }
    }


    public void handleValidate() {
        String userName = userNameField.getText();
        String password = passwordField.getText();


        if (userName.isBlank()) {
            validateStatusLabel.setTextFill(Color.RED);
            validateStatusLabel.setText("Username cannot be empty");
        } else if (password.isBlank()) {
            validateStatusLabel.setTextFill(Color.RED);
            validateStatusLabel.setText("Password cannot be empty");
        } else {
            if (!userName.equals(this.username)) {
                validateStatusLabel.setTextFill(Color.RED);
                validateStatusLabel.setText("Invalid Username for current user");
                return;
            }
            boolean isAuthenticated = DatabaseHandler.authenticateUser(userName, password);
            if (!isAuthenticated) {
                validateStatusLabel.setTextFill(Color.RED);
                validateStatusLabel.setText("Invalid Username/Password");
                return;
            }

            this.username = userName;
            newUserNameField.setText(userName);
            newPasswordField.setText(password);
            validateForm.setVisible(false);
            validateForm.setManaged(false);
            editAccountForm.setVisible(true);
            editAccountForm.setManaged(true);
        }

    }

}
