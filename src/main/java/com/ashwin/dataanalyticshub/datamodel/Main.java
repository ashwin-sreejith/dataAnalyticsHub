package com.ashwin.dataanalyticshub.datamodel;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// main
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SocialMediaOperations operations = new SocialMediaOperations();
        // path to csv
        String path = "resources/posts.csv";
        // set to store and match unique ID
        Set<Integer> uniqueId= new HashSet<>();
        FileHandler file = new FileHandler(path, operations, uniqueId);
        Menu menu = new Menu(scanner, operations, uniqueId);

        try{
            // stores if file load failed
            boolean loadFailed = file.readFile();
            if (!loadFailed)
                // menu shown only if file load succeeded
                menu.displayMenu();
            else
                System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}