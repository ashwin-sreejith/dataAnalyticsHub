package com.ashwin.dataanalyticshub.datamodel;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileHandlerTest {

    private FileHandler fileHandler;
    @BeforeEach
    void setUp() {
        // Initialize FileHandler instance
        fileHandler = FileHandler.getInstance();
        // Set the username for testing
        fileHandler.setUsername("testUser");
    }

    @Test
    void loadPostsFromCSV() {
        String filePath = "resources/posts.csv";

        // Load posts from the CSV file
        List<SocialMediaPost> posts = fileHandler.loadPostsFromCSV(filePath);

        // Check if posts were loaded successfully
        assertNotNull(posts);
        assertEquals(5, posts.size());
    }
}