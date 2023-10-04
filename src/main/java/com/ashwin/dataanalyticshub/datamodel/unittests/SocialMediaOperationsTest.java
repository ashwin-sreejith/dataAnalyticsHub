package com.ashwin.dataanalyticshub.datamodel.unittests;

import com.example.socialmediaanalyzer.CustomExceptions.PostCollectionEmptyException;
import com.example.socialmediaanalyzer.CustomExceptions.PostNotFoundException;
import com.example.socialmediaanalyzer.SocialMediaOperations;
import com.example.socialmediaanalyzer.SocialMediaPost;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

// tests post operations class
public class SocialMediaOperationsTest {
    private static SocialMediaOperations operations;
    @BeforeAll
    static void setup() {
        operations = new SocialMediaOperations();
    }

    // generates a dummy post
    private HashMap<String, Object> createPostDetails(boolean batch) {
        HashMap<String, Object> postDetails = new HashMap<>();
        postDetails.put("postId", 2);
        if (batch)
            postDetails.put("content", "Batch Test Content");
        else
            postDetails.put("content", "Test Content");
        postDetails.put("author", "Test Author");
        postDetails.put("likes", 18);
        postDetails.put("shares", 4);
        return postDetails;
    }


    // tests if non batch posts are added
    @Test
    void addNewPostNonBatch() {

        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());
        operations.addNewPost(map, false);

        // Assert that the post with postId 2 was added to the postCollection
        assertTrue(operations.getPostCollection().containsKey(2), "Post with ID 2 not found in collection");

        // Assert that the added post details match the input details
        SocialMediaPost addedPost = operations.getPostCollection().get(2);
        assertEquals("Test Content", addedPost.getContent(), "Post details doesn't match");
        assertEquals("Test Author", addedPost.getAuthor(), "Post details doesn't match");
        assertEquals(18, addedPost.getLikes(), "Post details doesn't match");
        assertEquals(4, addedPost.getShares(), "Post details doesn't match");
        assertEquals(map.get("dateTime"), addedPost.getDateTime(), "Post details doesn't match");
    }

    // tests if posts are added in batch from csv
    @Test
    void addNewPostBatch() {
        HashMap<String, Object> map = createPostDetails(true);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, true);

        // Assert that the post with postId 2 was added to the postCollection
        assertTrue(operations.getPostCollection().containsKey(2), "Post with ID 2 not found in collection");

        // Assert that the added post details match the input details
        SocialMediaPost addedPost = operations.getPostCollection().get(2);
        assertEquals("Batch Test Content", addedPost.getContent(), "Post details doesn't match");
        assertEquals("Test Author", addedPost.getAuthor(), "Post details doesn't match");
        assertEquals(18, addedPost.getLikes(), "Post details doesn't match");
        assertEquals(4, addedPost.getShares(), "Post details doesn't match");
        assertEquals(map.get("dateTime"), addedPost.getDateTime(), "Post details doesn't match");
    }


    // tests if existing posts are deleted
    @Test
    void deletePostExisting() throws PostCollectionEmptyException, PostNotFoundException {
        HashMap<String, Object> map = createPostDetails(true);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, true);

        operations.deletePost(2);
        // asserts that post id doesn't exist after deleting
        assertFalse(operations.getPostCollection().containsKey(2), "Post with ID 2 found in collection");
    }

    // tests if non-existing post id deletion is handled
    @Test
    void deletePostNonExisting() {
        // asserts that exception will be thrown if non-existing post ID provided for deleting
        assertThrows(PostNotFoundException.class, ()->operations.deletePost(200),
                "Exception PostNotFound not thrown");
    }

    // tests if deleting from an empty collection is handled
    @Test
    void deletePostWhenEmpty() throws PostCollectionEmptyException, PostNotFoundException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, false);

        operations.deletePost(2);
        // asserts that exception will be thrown if post collection is empty
        assertThrows(PostCollectionEmptyException.class, ()->operations.deletePost(200),
                "Exception PostCollectionEmpty not thrown");
    }

    // tests if post retrieval for existing post id works
    @Test
    void retrievePostExisting() throws PostCollectionEmptyException, PostNotFoundException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, false);
        // asserts null Junit executable will be returned if all went well
        assertNull(operations.retrievePost(2), "Exceptions were thrown");
    }

    // tests if post retrieval of non-existing ID is handled
    @Test
    void retrievePostNonExisting() {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, false);
        // asserts exception will be thrown
        assertThrows(PostNotFoundException.class, ()->operations.retrievePost(200),
                "Exception PostNotFound not thrown");
    }

    // tests if retrieval from empty collection is handled
    @Test
    void retrievePostWhenEmpty() throws PostCollectionEmptyException, PostNotFoundException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, false);
        operations.deletePost(2);
        // asserts exception will be thrown
        assertThrows(PostCollectionEmptyException.class, ()->operations.retrievePost(200),
                "Exception PostCollectionEmpty not thrown");
    }

    // tests if sorting posts by likes works
    @Test
    void getTopNPostsByLikes() throws PostCollectionEmptyException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());

        operations.addNewPost(map, false);
        // asserts null Junit executable will be returned if all went well
        assertNull(operations.getTopNPostsByLikes(55), "Exceptions were thrown");
    }

    // tests if sorting is handled if collection is empty
    @Test
    void getTopNPostsByLikesWhenEmpty() throws PostCollectionEmptyException, PostNotFoundException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());
        operations.deletePost(2);
        // asserts exception will be thrown
        assertThrows(PostCollectionEmptyException.class, ()->operations.getTopNPostsByLikes(55),
                "PostCollectionEmpty not thrown");
    }

    // tests if sorting by shares works
    @Test
    void getTopNPostsByShares() throws PostCollectionEmptyException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());
        operations.addNewPost(map, false);
        // asserts null Junit executable will be returned if all went well
        assertNull(operations.getTopNPostsByShares(55), "Exceptions were thrown");
    }

    // tests if sorting is handled when collection is empty
    @Test
    void getTopNPostsBySharesWhenEmpty() throws PostCollectionEmptyException, PostNotFoundException {
        HashMap<String, Object> map = createPostDetails(false);
        map.put("dateTime", LocalDateTime.now());
        operations.deletePost(2);
        // asserts exception will be thrown
        assertThrows(PostCollectionEmptyException.class, ()->operations.getTopNPostsByShares(55),
                "Exceptions not thrown");
    }
}