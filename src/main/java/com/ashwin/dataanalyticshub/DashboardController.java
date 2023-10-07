package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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
    public VBox dynamicWindow;
    @FXML
    private Label userLabel;

    @FXML
    private MenuButton optionsMenu;

    @FXML
    private TableView<?> postTableView;

    private String userName;

//    private HashMap<String, String> postDetails;



    @FXML
    public void initialize() {
        loadAllPostScene();
    }

    // Method to set the welcome message with the provided username
    public void setWelcomeMessage(String username) {
        this.userName = username;
        String fullName = DatabaseHandler.getFullNameByUsername(this.userName);
        userLabel.setText("Welcome, " + fullName +"!");
    }

    public void loadAddPostScene() {
        loadChildFXML("AddPost-view.fxml");
    }

    public void loadAllPostScene() {
        loadChildFXML("AllPost-view.fxml");
    }

    private void loadChildFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node child = loader.load();
            if(fxmlFileName.equals("AddPost-view.fxml")){
                AddPostController controller = loader.getController();
                controller.setUserName(this.userName);
            }
            // Clear existing content in the VBox
            dynamicWindow.getChildren().clear();
            // Set the loaded content into the VBox
            dynamicWindow.getChildren().add(child);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
