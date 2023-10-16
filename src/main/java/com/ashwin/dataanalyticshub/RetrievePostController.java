package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaOperations;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import com.ashwin.dataanalyticshub.datamodel.Util;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private ObservableList<SocialMediaPost> postsList = FXCollections.observableArrayList();



    @FXML
    public void initialize() {

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
            DatabaseHandler.saveRetrievedPost(this.username, post.getId());
            postsList = postTable.getItems();
            postsList.add(post);
            postTable.setItems(postsList);
            saveButton.setVisible(true);
            saveButton.setManaged(true);
        }
    }

    public void setUserName(String username) {

        this.username = username;
        List<SocialMediaPost> retrievedPostData = DatabaseHandler.getPostsForUser(this.username);
        this.postsList.addAll(retrievedPostData);
        postTable.setItems(postsList);
        if (!postsList.isEmpty()) {
            saveButton.setVisible(true);
            saveButton.setManaged(true);
        }
        else {
            saveButton.setVisible(false);
            saveButton.setManaged(false);
        }
    }

    public void handleSave() {
        exportToCSV();
    }

    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("retrievedPost.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File outputfile = fileChooser.showSaveDialog(null);

        if (outputfile != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputfile))) {

                writer.write("ID,Content,Author,Likes,Shares,DateTime\n");


                for (SocialMediaPost post : postTable.getItems()) {
                    writer.write(String.format("%d,%s,%s,%d,%d,%s\n", post.getId(), post.getContent(),
                            post.getAuthor(), post.getLikes(), post.getShares(), post.getDateTime()));
                }

                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
