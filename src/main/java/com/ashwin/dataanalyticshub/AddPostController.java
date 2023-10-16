package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.HashMap;

public class AddPostController {
    @FXML
    public GridPane addPostForm;
    @FXML
    public TextField postIDField;
    @FXML
    public TextField contentField;
    @FXML
    public TextField likesField;
    @FXML
    public TextField sharesField;
    @FXML
    public DatePicker dateField;
    @FXML
    public Spinner spinnerHour;
    @FXML
    public Spinner spinnerMinutes;
    @FXML
    public Label userMessage;

    private String userName;

    private final HashMap<String, String> postDetails = new HashMap<>();

    public void setUserName(String name) {
        this.userName = name;
    }

    public void handleAddPost() {

        SocialMediaOperations operations = SocialMediaOperations.getInstance();
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
        System.out.println("BFR:"+dateTime);

        postDetails.put("postId", id);
        postDetails.put("content", content);
        postDetails.put("author", this.userName);
        postDetails.put("likes", likes);
        postDetails.put("shares", shares);
        postDetails.put("dateTime", dateTime);
        String message = operations.addNewPost(postDetails);
        if (message.equals("Success")) {
            userMessage.setTextFill(Color.GREEN);
            userMessage.setText("Post Added Successfully");
        } else {
            userMessage.setText(message);
        }
    }
}
