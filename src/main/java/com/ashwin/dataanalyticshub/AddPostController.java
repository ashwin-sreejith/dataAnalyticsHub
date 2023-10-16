package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import java.util.HashMap;

public class AddPostController {
    public GridPane addPostForm;
    public TextField postIDField;
    public TextField contentField;
    public TextField likesField;
    public TextField sharesField;
    public DatePicker dateField;
    public Spinner spinnerHour;
    public Spinner spinnerMinutes;
    public Label userMessage;

    private String userName;

    private final HashMap<String, String> postDetails = new HashMap<>();

    public void setUserName(String name) {
        this.userName = name;
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
        System.out.println("BFR:"+dateTime);

        postDetails.put("postId", id);
        postDetails.put("content", content);
        postDetails.put("author", this.userName);
        postDetails.put("likes", likes);
        postDetails.put("shares", shares);
        postDetails.put("dateTime", dateTime);
        String message = x.addNewPost(postDetails);
        if (message.equals("Success")) {
            userMessage.setTextFill(Color.GREEN);
            userMessage.setText("Post Added Successfully");
        } else {
            userMessage.setText(message);
        }
    }
}
