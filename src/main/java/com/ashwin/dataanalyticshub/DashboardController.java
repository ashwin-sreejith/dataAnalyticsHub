package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    public VBox dynamicWindow;
    public Button logoutButton;

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
        fullName = String.valueOf(titleCase(fullName));
        userLabel.setText("Welcome, " + fullName);
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

    public void logout() {
        loadLoginView();
    }
    private void loadLoginView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login-view.fxml"));
            Parent loginParent = loader.load();
            Scene loginScene = new Scene(loginParent, 800, 800);

            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private StringBuilder titleCase(String fullName) {
        boolean capitalise = true;
        StringBuilder s = new StringBuilder();
        for (char c : fullName.strip().toCharArray()) {
            if (c == 32) {
                capitalise = true;
                s.append(" ");
                continue;
            }

            if (capitalise) {
                c = Character.toUpperCase(c);
                s.append(c);
                capitalise = false;
            } else {
                s.append(c);
            }



        }
        System.out.println(s);
        return s;
    }
}
