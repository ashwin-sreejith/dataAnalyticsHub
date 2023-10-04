package com.ashwin.dataanalyticshub.datamodel;

import com.example.socialmediaanalyzer.CustomExceptions.PostCollectionEmptyException;
import com.example.socialmediaanalyzer.CustomExceptions.PostNotFoundException;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
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
    public void addNewPost(HashMap<String, Object> postDetails, boolean batch) {
        int postId = (int) postDetails.get("postId");
        String content = (String) postDetails.get("content");
        String author = (String) postDetails.get("author");
        int likes = (int) postDetails.get("likes");
        int shares = (int) postDetails.get("shares");
        LocalDateTime dateTime = (LocalDateTime) postDetails.get("dateTime");

        SocialMediaPost newPost = new SocialMediaPost(postId, content, author, likes, shares, dateTime);
        this.postCollection.put(postId, newPost);

        if (!batch) {
            System.out.println("The post has been added to the collection!");
        }
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
    public Executable retrievePost(int postId) throws PostNotFoundException, PostCollectionEmptyException {
        if (postCollection.isEmpty()) {
            throw new PostCollectionEmptyException("Post collection empty! Please add posts first!");
        }

        SocialMediaPost post = postCollection.get(postId);
        if (post == null) {
            throw new PostNotFoundException("Sorry the post does not exist in the collection!");
        }

        String boundary = "+--------+-------------------------------------------------------------------------------" +
                "---+----------+-------+--------+------------------+";
        String columnHeader = "|\u001B[34;1m   ID   \u001B[0m|\u001B[34;1m Content                                   " +
                "                                       \u001B[0m|\u001B[34;1m Author   \u001B[0m|\u001B[34;1m Likes " +
                "\u001B[0m|\u001B[34;1m Shares \u001B[0m|\u001B[34;1m Date and Time    \u001B[0m|";
        System.out.println(boundary);
        System.out.println(columnHeader);
        System.out.println(post);
        System.out.println(boundary);

        return null;
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

}
