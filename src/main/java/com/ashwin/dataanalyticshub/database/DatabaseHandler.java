package com.ashwin.dataanalyticshub.database;
import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import com.ashwin.dataanalyticshub.datamodel.Util;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Handles all database related operations
public class DatabaseHandler {
    private static final String DATABASE_URL = "jdbc:sqlite:database/dataHub.db";

    // Connect to database
    public static Connection connect() {
        try {

            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.err.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }

    // Disconnect an established connection
    public static void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed closing database connection: " + e.getMessage());
        }
    }

    // Authenticates users
    public static boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                ResultSet resultSet = preparedStatement.executeQuery();

                return resultSet.next(); // If a row is returned, the authentication is successful
            }
        } catch (SQLException e) {
            System.err.println("Error during authentication: " + e.getMessage());
            return false;
        }
    }

    // Inserts a post into postCollection table
    public static int insertPost(SocialMediaPost post) {
        String insertSQL = "INSERT INTO postCollection (id, content, userId, likes, shares, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                preparedStatement.setInt(1, post.getId());
                preparedStatement.setString(2, post.getContent());
                preparedStatement.setString(3, post.getAuthor());
                preparedStatement.setInt(4, post.getLikes());
                preparedStatement.setInt(5, post.getShares());
                LocalDateTime dateTime = post.getDateTime();
                String formattedDate = Util.dateFormatterFunc(dateTime);
                preparedStatement.setString(6, formattedDate);
                preparedStatement.executeUpdate();
                System.out.println("Post inserted into the database.");
                return 200;
            } catch (SQLException e) {
                if (e.getErrorCode() == 19) {
                    System.err.println("Post with the same ID already exists.");
                    return 19;
                } else {
                    System.err.println("Error inserting post into the database: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting post into the database: " + e.getMessage());
        }
        return -1;
    }

    // Inserts a user into users table
    public static int insertUser(String firstName, String lastName, String username, String password) {
        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                         "INSERT INTO users (firstname, lastname, username, password) VALUES (?, ?, ?, ?)")) {

                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, password);
                preparedStatement.executeUpdate();
                System.out.println("User registered successfully.");
                return 200;
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                System.err.println("Username already exists.");
                return 19;
            } else {
                System.err.println("Error inserting user into the database: " + e.getMessage());
            }
        }
        return -1;
    }

    // Returns fullname of user based on provided username
    public static String getFullNameByUsername(String username) {
        String fullName = null;
        Connection connection = connect();

        if (connection != null) {
            try {
                String query = "SELECT firstname, lastname FROM users WHERE username=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String firstName = resultSet.getString("firstname");
                    String lastName = resultSet.getString("lastname");
                    fullName = firstName + " " + lastName;
                }

                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                disconnect(connection);
            }
        }

        return fullName;
    }


    // Retrieves a post using PostID
    public static SocialMediaPost retrievePostById(int postId, String username) {
        String query = "SELECT * FROM postCollection WHERE id = ? AND userId = ?";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setInt(1, postId);
                preparedStatement.setString(2, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String content = resultSet.getString("content");
                        int likes = resultSet.getInt("likes");
                        int shares = resultSet.getInt("shares");
                        String date = resultSet.getString("date");
                        LocalDateTime dateTime = Util.localDateTimeFormatFunc(date);

                        return new SocialMediaPost(id, content, username, likes, shares, dateTime);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Fetch account details based username
    public static HashMap<String, String> fetchAccountDetails(String username) {
        String query = "SELECT * FROM users WHERE username=?";
        HashMap<String, String> userDetails= new HashMap<>();

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        userDetails.put("username", resultSet.getString("username"));
                        userDetails.put("firstname", resultSet.getString("firstname"));
                        userDetails.put("lastname", resultSet.getString("lastname"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user details: " + e.getMessage());
        }

        return userDetails;

    }

    // Checks if username exists in users table
    public static boolean doesUsernameExist(String username) {
        String query = "SELECT COUNT(*) AS count FROM users WHERE username = ?";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }

            }
        } catch (SQLException e) {
            System.err.println("Error checking username existence: " + e.getMessage());
        }
        return false;
    }

    // Updates username and all references of that user in other tables
    public static boolean updateUsernameAndReferences(String oldUsername, String newUsername, String password, String firstname, String lastname) {
        String updateUsersQuery = "UPDATE users SET username = ?, firstname = ?, lastname = ?, password = ? WHERE username = ?";
        String updatePostCollectionQuery = "UPDATE postCollection SET userId = ? WHERE userId = ?";

        try (Connection connection = connect()) {
            assert connection != null;
            connection.setAutoCommit(false);


            try (PreparedStatement preparedStatementUsers = connection.prepareStatement(updateUsersQuery)) {
                preparedStatementUsers.setString(1, newUsername);
                preparedStatementUsers.setString(2, firstname);
                preparedStatementUsers.setString(3, lastname);
                preparedStatementUsers.setString(4, password);
                preparedStatementUsers.setString(5, oldUsername);
                preparedStatementUsers.executeUpdate();
            }


            try (PreparedStatement preparedStatementPostCollection = connection.prepareStatement(updatePostCollectionQuery)) {
                preparedStatementPostCollection.setString(1, newUsername);
                preparedStatementPostCollection.setString(2, oldUsername);
                preparedStatementPostCollection.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);

            System.out.println("Username and references updated successfully.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error updating username and references: " + e.getMessage());
        }

        return false;
    }

    // Returns VIP status of user
    public static boolean isVip(String username) {
        String query = "SELECT vip FROM users WHERE username = ?";
        boolean isVip = false;

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int vipValue = resultSet.getInt("vip");
                        isVip = vipValue == 1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isVip;
    }

    // Set a given user as VIP
    public static void setVipForUser(String username) {
        String query = "UPDATE users SET vip = 1 WHERE username = ?";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, username);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("VIP status updated for user: " + username);
                } else {
                    System.out.println("User not found or VIP status could not be updated.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating VIP status for user: " + username);
            e.printStackTrace();
        }

    }

    // Saves the state of retrieved posts for each user
    public static void saveRetrievedPost(String userId, int postId) {
        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                         "INSERT INTO user_retrieved_posts (userId, postId) VALUES (?, ?)")) {

                preparedStatement.setString(1, userId);
                preparedStatement.setInt(2, postId);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all post ID's already retrieved by user
    private static List<Integer> getUserRetrievedPostIds(String userId) {
        List<Integer> postIds = new ArrayList<>();
        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT postId FROM user_retrieved_posts WHERE userId = ?")) {

                preparedStatement.setString(1, userId);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    postIds.add(resultSet.getInt("postId"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return postIds;
    }

    // Fetch all the posts retrieved by user
    public static List<SocialMediaPost> getPostsForUser(String userId) {
        List<SocialMediaPost> posts = new ArrayList<>();

        try (Connection connection = connect()) {
            // Get post IDs for the user
            List<Integer> postIds = getUserRetrievedPostIds(userId);

            // Fetch the posts using post IDs
            for (Integer postId : postIds) {
                try {
                    assert connection != null;
                    try (PreparedStatement preparedStatement = connection.prepareStatement(
                            "SELECT * FROM postCollection WHERE id = ?")) {

                        preparedStatement.setInt(1, postId);
                        ResultSet resultSet = preparedStatement.executeQuery();

                        if (resultSet.next()) {

                            int id = resultSet.getInt("id");
                            String content = resultSet.getString("content");
                            String author = resultSet.getString("userId");
                            int likes = resultSet.getInt("likes");
                            int shares = resultSet.getInt("shares");
                            String date = resultSet.getString("date");
                            LocalDateTime dateTime = Util.localDateTimeFormatFunc(date);

                            // Create a SocialMediaPost object and add to the list
                            SocialMediaPost post = new SocialMediaPost(id, content, author, likes, shares, dateTime);
                            posts.add(post);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return posts;
    }

    // Delete a post from table using its ID
    public static boolean deletePostById(int postId) {
        try (Connection connection = connect()) {
            String query = "DELETE FROM postCollection WHERE id = ?";
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, postId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    deletePostEntryInRetrieved(postId);
                    System.out.println("Post with ID " + postId + " deleted successfully.");
                    return true;
                } else {
                    System.out.println("No post found with ID " + postId);
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting post: " + e.getMessage());
        }
        return false;
    }

    // Delete a post from retrieved posts
    private static void deletePostEntryInRetrieved(int postId) {
        String query = "DELETE FROM user_retrieved_posts WHERE postId = ?";
        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement deleteUserRetrievedStatement = connection.prepareStatement(query)) {
                deleteUserRetrievedStatement.setInt(1, postId);
                int userRetrievedDeleted = deleteUserRetrievedStatement.executeUpdate();
                if (userRetrievedDeleted > 0) {
                    System.out.println("Entry for post ID " + postId + " deleted from user_retrieved_posts table.");
                } else {
                    System.out.println("No entry found for post ID " + postId + " in user_retrieved_posts table.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting post: " + e.getMessage());
        }
    }

    // Retrieve the top N posts of a user based on the likes
    public static List<SocialMediaPost> getTopNPostsByUser(int n, String userId) {
        List<SocialMediaPost> topPosts = new ArrayList<>();
        try (Connection connection = connect()) {
            String query = "SELECT * FROM postCollection WHERE userId = ? ORDER BY likes DESC LIMIT ?";
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userId);
                statement.setInt(2, n);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    int likes = resultSet.getInt("likes");
                    int shares = resultSet.getInt("shares");
                    String date = resultSet.getString("date");
                    LocalDateTime dateTime = Util.localDateTimeFormatFunc(date);

                    SocialMediaPost post = new SocialMediaPost(id, content, userId, likes, shares, dateTime);
                    topPosts.add(post);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving top N posts by user: " + e.getMessage());
        }

        return topPosts;
    }

    // Get the number of posts made by user
    public static int getPostCountForUser(String username) {
        int postCount = 0;

        try (Connection connection = connect()) {
            String query = "SELECT COUNT(*) AS postCount FROM postCollection WHERE userId = ?";
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    postCount = resultSet.getInt("postCount");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting post count for user: " + e.getMessage());
        }

        return postCount;
    }

    // Retrieve all posts made by user
    public static List<SocialMediaPost> getAllPostsByUser(String username) {
        List<SocialMediaPost> userPosts = new ArrayList<>();

        try (Connection connection = connect()) {
            String query = "SELECT * FROM postCollection WHERE userId = ?";
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    String author = resultSet.getString("userId");
                    int likes = resultSet.getInt("likes");
                    int shares = resultSet.getInt("shares");
                    String date = resultSet.getString("date");
                    LocalDateTime dateTime = Util.localDateTimeFormatFunc(date);

                    SocialMediaPost post = new SocialMediaPost(id, content, author, likes, shares, dateTime);
                    userPosts.add(post);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting posts for user: " + e.getMessage());
        }

        return userPosts;
    }

}