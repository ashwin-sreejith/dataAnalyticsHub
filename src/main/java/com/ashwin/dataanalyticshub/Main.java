package com.ashwin.dataanalyticshub;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.FileHandler;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 800);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

        // Load posts from CSV
        FileHandler fileHandler = new FileHandler();
        List<SocialMediaPost> posts = fileHandler.loadPostsFromCSV("resources/posts.csv");

        // Insert posts into the database
        for (SocialMediaPost post : posts) {
            DatabaseHandler.insertPost(post);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}