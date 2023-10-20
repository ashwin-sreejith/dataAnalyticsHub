package com.ashwin.dataanalyticshub.datamodel;
import com.ashwin.dataanalyticshub.customexceptions.InvalidLineException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Handles bulk fetching the posts from file
public class FileHandler {
    private String username;
    private static FileHandler instance;

    // Use a single instance of this class throughout the application (SINGLETON)
    public static FileHandler getInstance() {
        if (instance == null) {
            instance = new FileHandler();
        }
        return instance;
    }

    // Sets the current user
    public void setUsername(String username) {
        this.username = username;
    }

    // Loads all posts from given csv excluding posts in incorrect format
    public List<SocialMediaPost> loadPostsFromCSV(String filePath) {
        List<SocialMediaPost> posts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {

                try {
                    // invokes validation function to validate each line for errors
                    if (!Util.isValidLine(line)) {
                        throw new InvalidLineException("\u001B[31mBad Entry[SKIPPED]\u001B[0m: " + line);
                    }

                    String[] post_data = line.split(",");
                    System.out.println(Arrays.toString(post_data));
                    int id = Integer.parseInt(post_data[0]);
                    String content = post_data[1];
                    String author = post_data[2];
                    // Resets author to current user
                    // Program has access controls for each user, only posts made by current author can be analyzed
                    // by current user. Hence, the author name is reset to username
                    if (!author.equalsIgnoreCase(this.username)){
                        author = this.username;
                    }
                    System.out.println("AUTHOR: "+author);
                    int likes = Integer.parseInt(post_data[3]);
                    int shares = Integer.parseInt(post_data[4]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
                    LocalDateTime localDateTime = LocalDateTime.parse(post_data[5], formatter);
                    SocialMediaPost post = new SocialMediaPost(id, content, author, likes, shares, localDateTime);
                    posts.add(post);
                } catch (InvalidLineException e){
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return posts;
    }
}
