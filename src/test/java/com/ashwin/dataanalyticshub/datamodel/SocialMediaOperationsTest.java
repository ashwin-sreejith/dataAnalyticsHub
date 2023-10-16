package com.ashwin.dataanalyticshub.datamodel;

import com.ashwin.dataanalyticshub.customexceptions.*;
import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SocialMediaOperationsTest {
    private SocialMediaOperations socialMediaOperations;
    @BeforeEach
    void setUp() {
        socialMediaOperations = SocialMediaOperations.getInstance();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
    }

    @Test
    void retrievePost() {
    }

    @Test
    void validator() {
        SocialMediaOperations socialMediaOperations = SocialMediaOperations.getInstance();

        HashMap<String, String> postDetails = new HashMap<>();
        postDetails.put("postId", "1");
        postDetails.put("content", "This is a post");
        postDetails.put("author", "user123");
        postDetails.put("likes", "10");
        postDetails.put("shares", "5");
        postDetails.put("dateTime", "2023/10/10 00:00");

        assertDoesNotThrow(() -> socialMediaOperations.validator(postDetails));
    }

    @Test
    void validatorErrorId() {
        SocialMediaOperations socialMediaOperations = SocialMediaOperations.getInstance();

        HashMap<String, String> postDetails = new HashMap<>();
        postDetails.put("postId", "aa");
        postDetails.put("content", "This is a post");
        postDetails.put("author", "user123");
        postDetails.put("likes", "10");
        postDetails.put("shares", "5");
        postDetails.put("dateTime", "2023/10/10 00:00");

        assertThrows(InvalidPostIdException.class, () -> socialMediaOperations.validator(postDetails));
    }

    @Test
    void validatorErrorContent() {
        SocialMediaOperations socialMediaOperations = SocialMediaOperations.getInstance();

        HashMap<String, String> postDetails = new HashMap<>();
        postDetails.put("postId", "5");
        postDetails.put("content", "This is, a post");
        postDetails.put("author", "user123");
        postDetails.put("likes", "10");
        postDetails.put("shares", "5");
        postDetails.put("dateTime", "2023/10/10 00:00");

        assertThrows(InvalidContentException.class, () -> socialMediaOperations.validator(postDetails));
    }

    @Test
    void validatorErrorLikes() {
        SocialMediaOperations socialMediaOperations = SocialMediaOperations.getInstance();

        HashMap<String, String> postDetails = new HashMap<>();
        postDetails.put("postId", "5");
        postDetails.put("content", "This is a post");
        postDetails.put("author", "user123");
        postDetails.put("likes", "A10");
        postDetails.put("shares", "5");
        postDetails.put("dateTime", "2023/10/10 00:00");

        assertThrows(InvalidLikesException.class, () -> socialMediaOperations.validator(postDetails));
    }

    @Test
    void validatorErrorShares() {
        SocialMediaOperations socialMediaOperations = SocialMediaOperations.getInstance();

        HashMap<String, String> postDetails = new HashMap<>();
        postDetails.put("postId", "5");
        postDetails.put("content", "This is a post");
        postDetails.put("author", "user123");
        postDetails.put("likes", "10");
        postDetails.put("shares", "5K");
        postDetails.put("dateTime", "2023/10/10 00:00");

        assertThrows(InvalidSharesException.class, () -> socialMediaOperations.validator(postDetails));
    }

    @Test
    void validatorErrorDateTime() {
        SocialMediaOperations socialMediaOperations = SocialMediaOperations.getInstance();

        HashMap<String, String> postDetails = new HashMap<>();
        postDetails.put("postId", "5");
        postDetails.put("content", "This is a post");
        postDetails.put("author", "user123");
        postDetails.put("likes", "10");
        postDetails.put("shares", "5");
        postDetails.put("dateTime", "2023-10-04T12:30");

        assertThrows(InvalidDateTimeException.class, () -> socialMediaOperations.validator(postDetails));
    }
}