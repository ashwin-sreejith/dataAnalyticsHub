package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

public class DashboardController {
    @FXML
    public TextField postIDField;
    public TextField contentField;
    public TextField likesField;
    public TextField sharesField;
    public GridPane addPostForm;
    public Button addButton;
    public DatePicker dateField;
    @FXML
    public Label userMessage;
    @FXML
    public Spinner spinnerHour;
    @FXML
    public Spinner spinnerMinutes;
    @FXML
    private Label userLabel;

    @FXML
    private MenuButton optionsMenu;

    @FXML
    private TableView<?> postTableView;

    private String userName;

    private HashMap<String, String> postDetails;



    @FXML
    public void initialize() {
        postDetails = new HashMap<>();
        postTableView.setVisible(true);
        addPostForm.setVisible(false);
        addPostForm.setManaged(false);
    }

    // Method to set the welcome message with the provided username
    public void setWelcomeMessage(String username) {
        this.userName = username;
        String fullName = DatabaseHandler.getFullNameByUsername(this.userName);
        userLabel.setText("Welcome, " + fullName +"!");
    }

    public void handleAddPostPrerequisites() {
        toggleVisibility("add");
    }

    public void handleAddPost() {

        SocialMediaOperations x = new SocialMediaOperations();
        String id = postIDField.getText();
        String content = contentField.getText();
        String likes = likesField.getText();
        String shares = sharesField.getText();
        String dateTime = String.valueOf(dateField.getValue());
        String hours = String.valueOf(spinnerHour.getValue());
        String minutes = String.valueOf(spinnerMinutes.getValue());
        dateTime = dateTime.replace('-', '/');
        hours = (hours.length() == 1) ? "0" + hours : hours;
        minutes = (minutes.length() == 1) ? "0" + minutes : minutes;
        dateTime = dateTime + " " + hours + ":" + minutes;

        String formattedDateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            formattedDateTime = parsedDateTime.format(outputFormatter);
        } catch (DateTimeParseException e) {
            userMessage.setText("Invalid Date");
            return;
        }


        System.out.println(formattedDateTime);
        postDetails.put("postId", id);
        postDetails.put("content", content);
        postDetails.put("author", this.userName);
        postDetails.put("likes", likes);
        postDetails.put("shares", shares);
        postDetails.put("dateTime", formattedDateTime);
        String message = x.addNewPost(postDetails);
        if (message.equals("Success")) {
            userMessage.setText("Post Added Successfully");
        } else {
            userMessage.setText(message);
        }
    }

    public void toggleVisibility(String button){

        switch (button) {
            case "add":
                postTableView.setVisible(false);
                postTableView.setManaged(false);
                addPostForm.setVisible(true);
                addPostForm.setManaged(true);


        }

    }


}
