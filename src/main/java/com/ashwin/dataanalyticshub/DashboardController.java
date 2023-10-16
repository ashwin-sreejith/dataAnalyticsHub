package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.FileHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.*;
import java.io.File;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    public VBox dynamicWindow;
    public Button logoutButton;
    public VBox vipSubscription;
    public Button vipButton;
    public Button vipOnly;

    @FXML
    private Label userLabel;

    private String userName;
    private boolean isVip;


    @FXML
    public void initialize() {
        loadAllPostScene();
        vipOnly.setVisible(false);
        vipOnly.setManaged(false);
    }

    // Method to set the welcome message with the provided username
    public void setWelcomeMessage(String username) {
        this.userName = username;
        String fullName = DatabaseHandler.getFullNameByUsername(this.userName);
        isVip = DatabaseHandler.isVip(this.userName);
        vipToggle(isVip);
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

    public void loadDeletePost(){ loadChildFXML("Delete-view.fxml"); }
    public void loadSort() { loadChildFXML("TopNLikes-view.fxml"); }



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

            if (fxmlFileName.equals("Delete-view.fxml")) {
                DeletePostController controller = loader.getController();
                controller.setUserName(this.userName);
            }

            if (fxmlFileName.equals("TopNLikes-view.fxml")) {
                TopNLikesController controller = loader.getController();
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

    public void handleVip() {
        vipDialogPopup();
    }

    private void vipDialogPopup() {
        // Create a Stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initStyle(StageStyle.UNIFIED);
        popupStage.setTitle("Join VIP");
        popupStage.initOwner(vipButton.getScene().getWindow());

        VBox popupContainer = new VBox(10);
        HBox buttonContainer = new HBox(10);
        popupContainer.setPadding(new Insets(25));
        buttonContainer.setSpacing(10);
        popupContainer.setSpacing(20);

        Label label = new Label("Would you like to subscribe to the application for a monthly fee of $0?");
        label.setStyle("-fx-font-size: 18px");
        Button proceedButton = new Button("Proceed");
        Button closeButton = new Button("Close");
        proceedButton.setStyle("-fx-background-color: #05c005; -fx-text-fill: #FDF0F0; -fx-cursor: hand; -fx-font-size: 17px");
        closeButton.setStyle("-fx-background-color: #b90808; -fx-text-fill: #FDF0F0; -fx-cursor: hand; -fx-font-size: 17px");

        closeButton.setOnAction(event -> {
            if (closeButton.getText().equals("Logout"))
                logout();
            popupStage.close();
        });
        proceedButton.setOnAction(event -> {
            label.setText("Please log out and log in again to access VIP functionalities.");
            setVip(this.userName);
            proceedButton.setVisible(false);
            proceedButton.setManaged(false);
            closeButton.setText("Logout");
        });

        buttonContainer.getChildren().addAll(proceedButton, closeButton);
        popupContainer.getChildren().addAll(label, buttonContainer);

        Scene popupScene = new Scene(popupContainer, Color.WHITE);
        popupStage.setScene(popupScene);

        Window currentWindow = vipButton.getScene().getWindow();
        double windowWidth = currentWindow.getWidth();
        double windowHeight = currentWindow.getHeight();
        double xCord = currentWindow.getX() + (windowWidth - popupContainer.prefWidth(-1) - 300) / 2;
        double yCord = currentWindow.getY() + (windowHeight - popupContainer.prefHeight(-1)) / 2;
        popupStage.setX(xCord);
        popupStage.setY(yCord);

        popupStage.showAndWait();
    }

    public void vipToggle(boolean isVip) {
        if (isVip) {
            vipSubscription.setVisible(false);
            vipSubscription.setManaged(false);
            vipOnly.setVisible(true);
            vipOnly.setManaged(true);
        }
    }

    public void setVip(String username) {
        DatabaseHandler.setVipForUser(username);
    }

    public void loadPostFromFile() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // Set extension filters if needed (optional)
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the FileChooser and wait for user selection
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            FileHandler fileHandler = new FileHandler();
            fileHandler.setUsername(this.userName);
            List<SocialMediaPost> posts = fileHandler.loadPostsFromCSV(selectedFile.getAbsolutePath());
            // Insert posts into the database
            for (SocialMediaPost post : posts) {
                DatabaseHandler.insertPost(post);
            }
        } else {
            System.out.println("No file selected.");
        }
    }


}
