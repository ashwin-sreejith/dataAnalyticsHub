package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import com.ashwin.dataanalyticshub.datamodel.Util;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class RetrievePostController {
    public GridPane retrievePost;

    public TextField postId;
    public Label userMessage;
    public Button saveButton;
    @FXML
    private TableView<SocialMediaPost> postTable;
    @FXML
    private TableColumn<SocialMediaPost, Integer> idCol;
    @FXML
    private TableColumn<SocialMediaPost, String> contentCol;
    @FXML
    private TableColumn<SocialMediaPost, String> authorCol;
    @FXML
    private TableColumn<SocialMediaPost, Integer> likesCol;
    @FXML
    private TableColumn<SocialMediaPost, Integer> sharesCol;
    @FXML
    private TableColumn<SocialMediaPost, LocalDateTime> datesCol;

    private String username;



    @FXML
    public void initialize() {
        saveButton.setVisible(false);
        saveButton.setManaged(false);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        likesCol.setCellValueFactory(new PropertyValueFactory<>("likes"));
        sharesCol.setCellValueFactory(new PropertyValueFactory<>("shares"));
        datesCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        idCol.setPrefWidth(100);
        contentCol.setPrefWidth(250);
        likesCol.setPrefWidth(100);
        sharesCol.setPrefWidth(100);
        authorCol.setPrefWidth(100);
        datesCol.setPrefWidth(200);

    }
    public void handleRetrieval() {
        SocialMediaOperations x = new SocialMediaOperations();
        if (!Util.isValidInteger(postId.getText(), false)) {
            System.out.println(postId.getText());
            userMessage.setText("Post ID has to be a non-zero Integer");
            return;
        }
        SocialMediaPost post = x.retrievePost(postId.getText(), this.username);
        if(post == null) {
            userMessage.setText("Post with ID " + postId.getText() + " doesn't exist!");
        }
        else {
            ObservableList<SocialMediaPost> postsList = postTable.getItems();
            postsList.add(post);
            postTable.setItems(postsList);
            saveButton.setVisible(true);
            saveButton.setManaged(true);
        }
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public void handleSave() {
        //TODO: to be implemented
    }
}
