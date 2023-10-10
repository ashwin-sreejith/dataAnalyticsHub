package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DashboardController {

    public VBox dynamicWindow;

    @FXML
    private Label userLabel;

    @FXML
    private MenuButton optionsMenu;

    private String userName;


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

    public void loadRetrieval(){ loadChildFXML("RetrievePost-view.fxml"); }
    public void loadEditAccount(){ loadChildFXML("EditAccount-view.fxml"); }


    private void loadChildFXML(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Node child = loader.load();
            if(fxmlFileName.equals("AddPost-view.fxml")){
                AddPostController controller = loader.getController();
                controller.setUserName(this.userName);
            }

            if (fxmlFileName.equals("EditAccount-view.fxml")) {
                EditAccountController controller = loader.getController();
                controller.setUserName(this.userName);
            }

            if (fxmlFileName.equals("RetrievePost-view.fxml")) {
                RetrievePostController controller = loader.getController();
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
