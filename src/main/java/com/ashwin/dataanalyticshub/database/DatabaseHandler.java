package com.ashwin.dataanalyticshub.database;

import java.sql.Connection;
import java.sql.DriverManager;
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

//    public static void insertPost(Post post) {
//        String insertSQL = "INSERT INTO posts (author, id, content, likes, shares) VALUES (?, ?, ?, ?, ?)";
//
//        try (Connection connection = connect();
//             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
//
//            preparedStatement.setString(1, post.getAuthor());
//            preparedStatement.setInt(2, post.getId());
//            preparedStatement.setString(3, post.getContent());
//            preparedStatement.setInt(4, post.getLikes());
//            preparedStatement.setInt(5, post.getShares());
//
//            preparedStatement.executeUpdate();
//            System.out.println("Post inserted into the database.");
//        } catch (SQLException e) {
//            System.err.println("Error inserting post into the database: " + e.getMessage());
//        }
//    }
}