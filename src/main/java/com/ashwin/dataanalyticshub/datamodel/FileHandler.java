package com.ashwin.dataanalyticshub.datamodel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public List<SocialMediaPost> loadPostsFromCSV(String filePath) {
        List<SocialMediaPost> posts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] post_data = line.split(",");

                int id = Integer.parseInt(post_data[0]);
                String content = post_data[1];
                String author = post_data[2];
                int likes = Integer.parseInt(post_data[3]);
                int shares = Integer.parseInt(post_data[4]);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(post_data[5], formatter);
                SocialMediaPost post = new SocialMediaPost(id, content, author, likes, shares, localDateTime);
                posts.add(post);
            }
        } catch (IOException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }

        return posts;
    }
}
