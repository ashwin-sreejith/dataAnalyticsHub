package com.ashwin.dataanalyticshub.datamodel;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import java.time.LocalDateTime;
import java.util.*;

// handles all operations that can be performed using posts
public class SocialMediaOperations {
    // TODO: Custom exception handling instead of string error messages
    // TODO: Date format to be with / instead of -
    // adds new posts to the collection
    public String addNewPost(HashMap<String, String> postDetails) {
        String validation = validator(postDetails);

        if(validation.equals("IDError")) {
            return "Id has to be a non-zero Integer.";
        }

        if(validation.equals("ContentError")) {
            return "Content cannot be empty.";
        }

        if(validation.equals("AuthorError")) {
            return "Invalid username! Contact Admin.";
        }

        if(validation.equals("LikesError")) {
            return "Like has to be a whole number.";
        }

        if(validation.equals("SharesError")) {
            return "Shares has to be a whole number.";
        }

        if(validation.equals("DateTimeError")) {
            return "Invalid Date and Time";
        }

        int postId = Integer.parseInt(postDetails.get("postId"));
        String content = postDetails.get("content");
        String author = postDetails.get("author");
        int likes = Integer.parseInt(postDetails.get("likes"));
        int shares = Integer.parseInt(postDetails.get("shares"));
        String dateTime = postDetails.get("dateTime");

        String formattedDateTime = Util.flipDate(dateTime);
        LocalDateTime date = Util.localDateTimeFormatFunc(formattedDateTime);
        SocialMediaPost newPost = new SocialMediaPost(postId, content, author, likes, shares, date);
        int errorCode = DatabaseHandler.insertPost(newPost);
        if(errorCode == 19) {
            return "Post Id Already Exists!";
        } else if (errorCode == -1) {
            return "Something Went Wrong! Contact Admin!";
        }

        return "Success";

    }

    // retrieves a post with its post ID if it exists and collection not empty
    public SocialMediaPost retrievePost(String postId, String username) {
        if(!Util.isValidInteger(postId, false)){
            return null;
        }
        SocialMediaPost post = DatabaseHandler.retrievePostById(Integer.parseInt(postId), username);

        if(post == null) {
            return null;
        }
        return post;

    }

    public String validator(HashMap<String, String> postDetails) {
        //TODO: Use custom exception classes
        String id = postDetails.get("postId");
        String content = postDetails.get("content");
        String author = postDetails.get("author");
        String likes = postDetails.get("likes");
        String shares = postDetails.get("shares");
        String date = postDetails.get("dateTime");

        String dateTime = Util.flipDate(date);
        System.out.println(dateTime);

        if(!Util.isValidInteger(id, false)) {
            return "IDError";
        }

        if(!Util.isValidString(content)) {
            return "ContentError";
        }

        if(!Util.isValidString(author)) {
            return "AuthorError";
        }

        if(!Util.isValidInteger(likes, true)) {
            return "LikesError";
        }

        if(!Util.isValidInteger(shares, true)) {
            return "SharesError";
        }

        if(!Util.isValidDateTime(dateTime)) {
            return "DateTimeError";
        }

        return "200";
    }

}
