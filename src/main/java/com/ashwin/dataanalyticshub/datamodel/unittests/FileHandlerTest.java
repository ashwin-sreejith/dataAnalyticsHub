package com.ashwin.dataanalyticshub.datamodel.unittests;

import com.example.socialmediaanalyzer.FileHandler;
import com.example.socialmediaanalyzer.SocialMediaOperations;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

// tests file handler class
public class FileHandlerTest {

    private final HashSet<FileHandler> fileHandlers = new HashSet<>();

    @Test
    void readFile() throws IOException {
        FileHandler handler = new FileHandler("resources/posts.csv", new SocialMediaOperations(), new HashSet<>());
        assertFalse(handler.readFile());
    }

    @Test
    void readFileNotFound(){
        FileHandler handler = new FileHandler("resources/post123.csv", new SocialMediaOperations(), new HashSet<>());
        assertThrows(IOException.class, handler::readFile);
    }


}