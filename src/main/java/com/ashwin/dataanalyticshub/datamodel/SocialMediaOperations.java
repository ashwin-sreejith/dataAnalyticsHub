package com.ashwin.dataanalyticshub.datamodel;

import com.ashwin.dataanalyticshub.database.DatabaseHandler;
import com.ashwin.dataanalyticshub.datamodel.customexceptions.PostCollectionEmptyException;
import com.ashwin.dataanalyticshub.datamodel.customexceptions.PostNotFoundException;
import org.junit.jupiter.api.function.Executable;

import javax.xml.validation.Validator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// handles all operations that can be performed using posts
public class SocialMediaOperations {

    // stores the posts as a hashmap with post ID as key
    private final Map<Integer, SocialMediaPost> postCollection;

    public SocialMediaOperations() {
        this.postCollection = new HashMap<>();
    }

    public Map<Integer, SocialMediaPost> getPostCollection() {
        return postCollection;
    }

    // Inner class for sorting by likes
    private static class SortByLikes implements Comparator<SocialMediaPost> {
        @Override
        public int compare(SocialMediaPost post1, SocialMediaPost post2) {
            return Integer.compare(post2.getLikes(), post1.getLikes());
        }
    }

    // Inner class for sorting by shares
    private static class SortByShares implements Comparator<SocialMediaPost> {
        @Override
        public int compare(SocialMediaPost post1, SocialMediaPost post2) {
            return Integer.compare(post2.getShares(), post1.getShares());
        }
    }

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
            return "Invalid DateTime! Contact Admin.";
        }

        int postId = Integer.parseInt(postDetails.get("postId"));
        String content = postDetails.get("content");
        String author = postDetails.get("author");
        int likes = Integer.parseInt(postDetails.get("likes"));
        int shares = Integer.parseInt(postDetails.get("shares"));
        String dateTime = postDetails.get("dateTime");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
        LocalDateTime date = LocalDateTime.parse(dateTime, formatter);
        SocialMediaPost newPost = new SocialMediaPost(postId, content, author, likes, shares, date);
        int errorCode = DatabaseHandler.insertPost(newPost);
        if(errorCode == 19) {
            return "Post Id Already Exists!";
        } else if (errorCode == -1) {
            return "Something Went Wrong! Contact Admin!";
        }

        return "Success";

    }

    // deletes a post with the given post ID if it exists and collection not empty
    public void deletePost(int postId) throws PostNotFoundException, PostCollectionEmptyException {
        if (postCollection.isEmpty()) {
            throw new PostCollectionEmptyException("Post collection empty! Please add posts first!");
        }
        SocialMediaPost post = postCollection.remove(postId);
        if (post == null) {
            throw new PostNotFoundException("Sorry the post does not exist in the collection!");
        }
        System.out.println("The post has been removed from the collection!");

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

    // sorts the post by given comparator and invokes the method that prints the results
    private void getTopNPosts(int n, Comparator<SocialMediaPost> comparator, boolean likes) throws PostCollectionEmptyException {

        if (postCollection.isEmpty()) {
            throw new PostCollectionEmptyException("Post collection empty!");
        }

        List<SocialMediaPost> sortedPosts = new ArrayList<>(postCollection.values());
        sortedPosts.sort(comparator);

        printPostTable(n, sortedPosts, likes);

    }

    // invokes the method to sort the posts by likes
    public Executable getTopNPostsByLikes(int n) throws PostCollectionEmptyException{
        Comparator<SocialMediaPost> sortByLikes = new SortByLikes();
        getTopNPosts(n, sortByLikes, true);
        return null;
    }

    // invokes the method to sort the posts by shares
    public Executable getTopNPostsByShares(int n) throws PostCollectionEmptyException {
        Comparator<SocialMediaPost> sortByShares = new SortByShares();
        getTopNPosts(n, sortByShares, false);
        return null;
    }

    // prints the posts as per the sort defined
    private void printPostTable(int n, List<SocialMediaPost> sortedPosts, boolean likes) {

        String boundary = "+--------+-------------------------------------------------------------------------------" +
                "---+----------+";

        String columnHeader = "|\u001B[34;1m   ID   \u001B[0m|\u001B[34;1m Content                                   " +
                "                                       \u001B[0m|\u001B[34;1m Shares   \u001B[0m|";


        if (likes){
            columnHeader = "|\u001B[34;1m   ID   \u001B[0m|\u001B[34;1m Content                                   " +
                    "                                       \u001B[0m|\u001B[34;1m Likes    \u001B[0m|";
        }

        int collectionLength = sortedPosts.size();
        // takes the minimum of collection size and requested size to avoid OutOfBoundsException
        int maxPosts = Math.min(n, collectionLength);
        System.out.println();

        if (n != collectionLength){
            System.out.println(sortedPosts.size()+ " post(s) found in collection.");
            System.out.println("Top-" + maxPosts + " with most " + ((likes)?"likes":"shares" + " are:"));
        }

        System.out.println(boundary);
        System.out.println(columnHeader);
        System.out.println(boundary);
        
        for (int i = 0; i < maxPosts; i++) {
            SocialMediaPost current = sortedPosts.get(i);
            if (likes){
                System.out.printf("| %-6s | %-80s | %-8s |",
                        current.getId(), current.getContent(), current.getLikes());
                System.out.println();
            } else {
                System.out.printf("| %-6s | %-80s | %-8s |",
                        current.getId(), current.getContent(), current.getShares());
                System.out.println();
            }
       }
        System.out.println(boundary);
        System.out.println();
    }

    public String validator(HashMap<String, String> postDetails) {
        String id = postDetails.get("postId");
        String content = postDetails.get("content");
        String author = postDetails.get("author");
        String likes = postDetails.get("likes");
        String shares = postDetails.get("shares");
        String date = postDetails.get("dateTime");

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

        if(!Util.isValidDateTime(date)) {
            return "DateTimeError";
        }

        return "200";
    }

}
