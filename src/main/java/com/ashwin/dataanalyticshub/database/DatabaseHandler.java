package com.ashwin.dataanalyticshub.database;

import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;
import com.ashwin.dataanalyticshub.datamodel.Util;

import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseHandler {
    private static final String DATABASE_URL = "jdbc:sqlite:database/dataHub.db";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.err.println("Connection Failed: " + e.getMessage());
            return null;
        }
    }

    public static void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Failed closing database connection: " + e.getMessage());
        }
    }

    public static void insertPost(SocialMediaPost post) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error inserting post into the database: " + e.getMessage());
        }
    }

    public static void insertUser(String firstName, String lastName, String username, String password) {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO users (firstname, lastname, username, password) VALUES (?, ?, ?, ?)")) {

            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
            System.out.println("User registered successfully.");
        } catch (SQLException e) {
            System.err.println("Error inserting user into the database: " + e.getMessage());
        }
    }

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

    public static SocialMediaPost retrievePostById(int postId) {
        String query = "SELECT * FROM postCollection WHERE id = ?"; // Replace with your table name

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, postId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String content = resultSet.getString("content");
                    String author = resultSet.getString("userId");
                    int likes = resultSet.getInt("likes");
                    int shares = resultSet.getInt("shares");
                    String date = resultSet.getString("date");
                    LocalDateTime dateTime = Util.localDateTimeFormatFunc(date);
                    System.out.println(dateTime);
                    return new SocialMediaPost(id, content, author, likes, shares, dateTime);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}