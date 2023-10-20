package com.ashwin.dataanalyticshub;
import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import com.ashwin.dataanalyticshub.datamodel.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import java.time.LocalDateTime;
import java.util.List;

public class TopNLikesController {
    @FXML
    public TextField sortNumber;
    @FXML
    public Label userMessage;

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

    private final ObservableList<SocialMediaPost> postsList = FXCollections.observableArrayList();
    private String userName;

    // loads table
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

    // sets current user
    public void setUserName(String username) {
        this.userName = username;
    }

    // retrieves posts of current user sorted by likes
    public void handleSort() {
        userMessage.setTextFill(Color.RED);
        if (!Util.isValidInteger(sortNumber.getText(), false)) {
            System.out.println(sortNumber.getText());
            userMessage.setText("Sort Number has to be a non-zero Integer");
            return;
        }

        List<SocialMediaPost> postList = DatabaseHandler.getTopNPostsByUser(Integer.parseInt(sortNumber.getText()),
                this.userName);
        if (postList.isEmpty()) {
            userMessage.setText("You have no posts in your collection. Please Add some.");
            return;
        }
        userMessage.setTextFill(Color.BLUE);
        userMessage.setText("Showing top "+postList.size()+" of "+DatabaseHandler.getPostCountForUser(this.userName)+" posts.");
        postsList.clear();
        postsList.addAll(postList);
        postTable.setItems(postsList);

    }




}
