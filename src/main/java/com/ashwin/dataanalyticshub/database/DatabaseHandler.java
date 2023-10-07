package com.ashwin.dataanalyticshub.database;

import com.ashwin.dataanalyticshub.datamodel.SocialMediaPost;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        String insertSQL = "INSERT INTO posts (id, content, userId, likes, shares, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connect()) {
            assert connection != null;
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

                preparedStatement.setInt(1, post.getId());
                preparedStatement.setString(2, post.getContent());
                preparedStatement.setString(3, post.getAuthor());
                preparedStatement.setInt(4, post.getLikes());
                preparedStatement.setInt(5, post.getShares());

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


}