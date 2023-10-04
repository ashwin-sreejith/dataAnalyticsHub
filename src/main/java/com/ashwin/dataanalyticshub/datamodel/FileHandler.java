package com.ashwin.dataanalyticshub.datamodel;

import com.ashwin.dataanalyticshub.datamodel.customexceptions.InvalidLineException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

// handles reading from file (.csv)
public class FileHandler {
    private final String path;
    private final HashMap<String, Object> postDetails;
    private final SocialMediaOperations operations;
    private final Set<Integer> uniqueId;

    public FileHandler(String filePath, SocialMediaOperations operations, Set<Integer> uniqueId) {
        this.path = filePath;
        this.operations = operations;
        this.postDetails = new HashMap<>();
        this.uniqueId = uniqueId;
    }

    // opens and reads from .csv file
    public boolean readFile() throws IOException {
        // reads from file
        BufferedReader obj = new BufferedReader(new FileReader(this.path));
        // flag to skip header of the csv file
        boolean skipHeader = true;
        // flag to track if file had bad entries
        boolean foundError = false;
        String line = obj.readLine();

        while (line != null){
            // skips header
            if (skipHeader){
                skipHeader = false;
                line = obj.readLine();
                continue;
            }

            try{
                // invokes validation function to validate each line for errors
                if (!Util.isValidLine(line)){
                    throw new InvalidLineException("\u001B[31mBad Entry[SKIPPED]\u001B[0m: " + line);
                }

                // splits the error free line into post details and adds post to hashmap
                String[] post_data = line.split(",");
                uniqueId.add(Integer.parseInt(post_data[0]));
                postDetails.put("postId", Integer.parseInt(post_data[0]));
                postDetails.put("content", post_data[1]);
                postDetails.put("author", post_data[2]);
                postDetails.put("likes", Integer.parseInt(post_data[3]));
                postDetails.put("shares", Integer.parseInt(post_data[4]));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(post_data[5], formatter);
                postDetails.put("dateTime", localDateTime);

                operations.addNewPost(postDetails, true);

            } catch (InvalidLineException e){
                System.out.println(e.getMessage());
                foundError = true;
                line = obj.readLine();
                // skips lines with bad entries
                // can break here, but current implementation prints all the bad entries for debugging
                continue;
            }

            line = obj.readLine();
        }

        if (foundError){
            System.out.println("\u001B[35mLOADED WITH ERRORS! Please FIX csv.\u001B[0m");
            obj.close();
            return foundError;
        }

        // returns false if no errors were found
        obj.close();
        return false;

    }
}