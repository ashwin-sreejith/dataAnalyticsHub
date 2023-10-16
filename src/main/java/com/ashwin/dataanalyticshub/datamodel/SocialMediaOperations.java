package com.ashwin.dataanalyticshub.datamodel;
import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.customexceptions.*;
import java.time.LocalDateTime;
import java.util.*;

// handles all operations that can be performed using posts
public class SocialMediaOperations {

    private static SocialMediaOperations instance;

    private SocialMediaOperations() {}

    public static synchronized SocialMediaOperations getInstance() {
        if (instance == null) {
            instance = new SocialMediaOperations();
        }
        return instance;
    }

    // adds new posts to the collection
    public String addNewPost(HashMap<String, String> postDetails) {

        try {
            String validation = validator(postDetails);
        } catch (InvalidPostIdException | InvalidAuthorException | InvalidContentException | InvalidDateTimeException |
                 InvalidSharesException | InvalidLikesException e) {
            return String.valueOf(e.getMessage());
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

    public String validator(HashMap<String, String> postDetails) throws InvalidPostIdException, InvalidAuthorException,
            InvalidContentException, InvalidDateTimeException, InvalidSharesException, InvalidLikesException {

        String id = postDetails.get("postId");
        String content = postDetails.get("content");
        String author = postDetails.get("author");
        String likes = postDetails.get("likes");
        String shares = postDetails.get("shares");
        String date = postDetails.get("dateTime");

        String dateTime = Util.flipDate(date);
        System.out.println(dateTime);

        if(!Util.isValidInteger(id, false)) {
            throw new InvalidPostIdException("Id has to be a non-zero Integer");
        }

        if(!Util.isValidString(content)) {
            throw new InvalidContentException("Content cannot be empty or have commas");
        }

        if(!Util.isValidString(author)) {
            throw new InvalidAuthorException("Invalid username! Contact Admin");
        }

        if(!Util.isValidInteger(likes, true)) {
            throw new InvalidLikesException("Like has to be a whole number");
        }

        if(!Util.isValidInteger(shares, true)) {
            throw new InvalidSharesException("Shares has to be a whole number");
        }

        if(!Util.isValidDateTime(dateTime)) {
            throw new InvalidDateTimeException("Invalid Date and Time");
        }

        return "200";
    }

}
