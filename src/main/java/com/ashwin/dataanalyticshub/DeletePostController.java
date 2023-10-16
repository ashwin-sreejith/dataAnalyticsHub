package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import com.ashwin.dataanalyticshub.datamodel.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class DeletePostController {

    public Label userMessage;
    public VBox inputOptions;
    public TextField postIdField;
    public VBox confirmationBox;
    public Label confirmationPrompt;
    public Button yesButton;
    public Button noButton;

    private String userName;

    @FXML
    public void initialize() {
        confirmationBox.setVisible(false);
        confirmationBox.setManaged(false);
    }

    public void setUserName(String username) {
        this.userName = username;
    }
    public void handleDelete() {
        userMessage.setTextFill(Color.RED);
        if (!Util.isValidInteger(postIdField.getText(), false)) {
            System.out.println(postIdField.getText());
            userMessage.setText("Post ID has to be a non-zero Integer");
            return;
        }
        SocialMediaOperations operations = SocialMediaOperations.getInstance();
        SocialMediaPost post = operations.retrievePost(postIdField.getText(), this.userName);
        if(post == null) {
            userMessage.setText("Post with ID " + postIdField.getText() + " doesn't exist!");
            return;
        }

        userMessage.setText("");
        confirmationBox.setVisible(true);
        confirmationBox.setManaged(true);
        inputOptions.setVisible(false);
        inputOptions.setManaged(false);
        confirmationPrompt.setText("Are you sure you want to delete Post with ID : "+postIdField.getText());

    }

    public void deletePost() {
        boolean success = DatabaseHandler.deletePostById(Integer.parseInt(postIdField.getText()));
        if (success) {
            userMessage.setTextFill(Color.GREEN);
            userMessage.setText("Post deleted Successfully");
            cancelDelete();
        }
    }

    public void cancelDelete() {
        confirmationBox.setVisible(false);
        confirmationBox.setManaged(false);
        inputOptions.setVisible(true);
        inputOptions.setManaged(true);
    }
}
