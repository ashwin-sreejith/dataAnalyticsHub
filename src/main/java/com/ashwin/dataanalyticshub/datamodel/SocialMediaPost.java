package com.ashwin.dataanalyticshub.datamodel;
import java.sql.Date;
import java.time.LocalDateTime;

// handles basic structure of a post
public class SocialMediaPost {
    private final int id;
    private final String content;
    private final String author;
    private final int likes;
    private final int shares;
    private final LocalDateTime dateTime;

    public SocialMediaPost(int id, String content, String author, int likes, int shares, LocalDateTime dateTime) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.likes = likes;
        this.shares = shares;
        this.dateTime = dateTime;
    }


    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public int getLikes() {
        return likes;
    }

    public int getShares() {
        return shares;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // prints the objects in required format
    @Override
    public String toString() {

        return String.format("| %-6s | %-80s | %-8s | %5d | %6d | %16s |",
                id, content, author, likes, shares, dateTime);
    }

}
