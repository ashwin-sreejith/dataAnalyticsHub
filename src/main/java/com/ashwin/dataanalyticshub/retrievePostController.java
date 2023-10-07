package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class retrievePostController {
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




    @FXML
    public void initialize() {
        saveButton.setVisible(false);
        saveButton.setManaged(false);

        // Initialize cell value factories to extract properties from SocialMediaPost
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        likesCol.setCellValueFactory(new PropertyValueFactory<>("likes"));
        sharesCol.setCellValueFactory(new PropertyValueFactory<>("shares"));
        datesCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));

        // Adjust the column widths (you can adjust these values accordingly)
        idCol.setPrefWidth(100);
        contentCol.setPrefWidth(300);
        likesCol.setPrefWidth(100);
        sharesCol.setPrefWidth(100);
        authorCol.setPrefWidth(100);
        datesCol.setPrefWidth(200);

    }
    public void handleRetrieval() {
        SocialMediaOperations x = new SocialMediaOperations();
        SocialMediaPost post = x.retrievePost(postId.getText());
        if(post == null) {
            userMessage.setText("Post with ID " + postId.getText() + " doesn't exist!");
        }
        else {
            ObservableList<SocialMediaPost> postsList = FXCollections.observableArrayList();
            postsList.add(post);

            // Set the items in the TableView
            postTable.setItems(postsList);
            saveButton.setVisible(true);
            saveButton.setManaged(true);
        }
    }

    public void handleSave() {

    }
}
