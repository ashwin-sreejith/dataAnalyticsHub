package com.ashwin.dataanalyticshub.datamodel;

import com.ashwin.dataanalyticshub.datamodel.customexceptions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

// displays menu and handles user inputs
public class Menu {
    private final SocialMediaOperations operations;
    private final Scanner scanner;
    private final HashMap<String, Object> postDetails;
    private final Set<Integer> uniqueId;

    public Menu(Scanner scanner, SocialMediaOperations operations, Set<Integer> uniqueId){
        this.operations = operations;
        this.scanner = scanner;
        this.postDetails = new HashMap<>();
        this.uniqueId = uniqueId;
    }

    // displays menu and calls respective function to retrieve and validates the input
    public void displayMenu() {

        System.out.println("Welcome to Social Media Analyzer!");
        while (true) {
            try {

                System.out.println("+----------------------------------------------------------+");
                System.out.println("|                        \u001B[34;1mMain Menu\u001B[0m                         |");
                System.out.println("+----------------------------------------------------------+");
                System.out.println("| \u001B[1;35m1\u001B[0m | Add a social media post                              |");
                System.out.println("| \u001B[1;35m2\u001B[0m | Delete an existing social media post                 |");
                System.out.println("| \u001B[1;35m3\u001B[0m | Retrieve a social media post                         |");
                System.out.println("| \u001B[1;35m4\u001B[0m | Retrieve the top N posts with most likes             |");
                System.out.println("| \u001B[1;35m5\u001B[0m | Retrieve the top N posts with most shares            |");
                System.out.println("| \u001B[1;35m6\u001B[0m | Exit                                                 |");
                System.out.println("+---+------------------------------------------------------+");
                System.out.print("Please select: ");

                // accepts user menu option
                String choiceInput = scanner.nextLine().trim();

                switch (choiceInput) {
                    // add post
                    case "1" -> {
                        retrieveAndValidatePostId(true);
                        retrieveAndValidateContent();
                        retrieveAndValidateAuthor();
                        retrieveAndValidateLikesAndShares(true);
                        retrieveAndValidateLikesAndShares(false);
                        retrieveAndValidateDateTime();
                        //operations.addNewPost(postDetails);
                    }
                    case "2" -> {
                        // delete post
                        try {
                            int postId = retrieveAndValidatePostId(false);
                            operations.deletePost(postId);
                        } catch (PostNotFoundException | PostCollectionEmptyException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    case "3" -> {
                        // retrieve post
                        try {
                            int postId = retrieveAndValidatePostId(false);
                            operations.retrievePost(postId);
                        } catch (PostNotFoundException | PostCollectionEmptyException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    case "4" -> {
                        // retrieve top-N posts with likes
                        try {
                            int N = retrieveAndValidateNInput();
                            operations.getTopNPostsByLikes(N);
                        } catch (PostCollectionEmptyException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    case "5" -> {
                        // retrieve top-N posts with shares
                        try {
                            int N = retrieveAndValidateNInput();
                            operations.getTopNPostsByShares(N);
                        } catch (PostCollectionEmptyException e) {
                            System.out.println(e.getMessage());
                        }

                    }
                    case "6" -> {
                        // exit menu
                        System.out.println("Thanks for using Social Media Analyzer!");
                        scanner.close();
                        System.exit(0);
                    }

                    // handles invalid user entries for menu options
                    default -> throw new InvalidMenuEntryException("Invalid choice! Please try again.");
                }
            } catch (InvalidMenuEntryException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    // retrieves and validates post ID, returns post ID if @param add is false otherwise return 1
    private int retrieveAndValidatePostId(boolean add) {
        while (true) {
            try {
                System.out.print("Please provide the post ID: ");
                String postIdInput = this.scanner.nextLine().trim();
                // calls validation function
                if (!Util.isValidInteger(postIdInput, false)) {
                    throw new InvalidPostIdException("Invalid Post ID: Post ID has to be a positive integer.");
                }

                int idInput = Integer.parseInt(postIdInput);

                if (!add) {
                    return idInput;
                }

                // checks if postID is unique, adds post ID to a set to check for unique entries
                if (uniqueId.contains(idInput)) {
                    throw new PostIdAlreadyExistsException("Post ID already exists. Please enter a unique post ID!");
                }

                // adds post id to hashmap
                postDetails.put("postId", idInput);
                uniqueId.add(idInput);
                return 1;

            } catch (InvalidPostIdException | PostIdAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // retrieves and validates content
    private void retrieveAndValidateContent() {
        while (true) {
            try {
                System.out.print("Please provide the post content: ");
                String content = scanner.nextLine().trim();
                // invokes validation function to check if the content is valid string without commas
                if (!Util.isValidString(content)) {
                    throw new InvalidContentException("Invalid Entry : Content cannot be empty or contain commas!");
                }
                // adds content to hashmap
                postDetails.put("content", content);
                break;
            } catch (InvalidContentException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // retrieves and validates author
    private void retrieveAndValidateAuthor() {
        while (true) {
            try {
                System.out.print("Please provide the post author: ");
                String author = scanner.nextLine().trim();
                // invokes validation function to check if the author is valid string without commas
                if (!Util.isValidString(author)) {
                    throw new InvalidAuthorException("Invalid Entry : Author cannot be empty or have commas!");
                }
                // adds author to hashmap
                postDetails.put("author", author);
                break;
            } catch (InvalidAuthorException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // retrieves and validates like count
    private void retrieveAndValidateLikesAndShares(boolean isLikes) {
        String message;
        while (true) {
            try {
                // if like count is being checked
                if (isLikes) {
                    System.out.print("Please provide the number of likes of the post: ");
                    message = "Invalid Entry : Like count has to be a positive integer!";
                }
                else {
                    System.out.print("Please provide the number of shares of the post: ");
                    message = "Invalid Entry : Share count has to be a positive integer!";
                }
                String likesOrSharesInput = scanner.nextLine().trim();
                // invokes validation function to check if like count is a positive integer
                if (!Util.isValidInteger(likesOrSharesInput, true)) {
                    throw new InvalidLikesOrSharesException(message);
                }
                // adds likes to hashmap
                postDetails.put((isLikes)?"likes":"shares", Integer.parseInt(likesOrSharesInput));
                break;
            } catch (InvalidLikesOrSharesException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // retrieves and validates DateTime in dd/MM/yyyy HH:mm
    private void retrieveAndValidateDateTime(){
        while (true) {
            try {
                System.out.print("Please provide the date and time of the post in the format of dd/MM/yyyy HH:mm :  ");
                String dateTime = scanner.nextLine().trim();
                // invoke validation function
                if (!Util.isValidDateTime(dateTime)) {
                    throw new InvalidDateTimeException("Invalid Entry : Please check the date and time!");
                }
                // uses DateTimeFormatter to match and parse the date in required format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm");
                LocalDateTime date = LocalDateTime.parse(dateTime, formatter);
                // adds datetime to hashmap
                postDetails.put("dateTime", date);
                break;
            } catch (InvalidDateTimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // retrieves and validates top-N value
    private int retrieveAndValidateNInput(){
        while (true){
            try {
                System.out.print("Please specify the number of posts to retrieve (N): ");
                String input = scanner.nextLine().trim();
                // invokes validation function
                if (!Util.isValidInteger(input, false)) {
                    throw new InvalidNPostException("Invalid Entry : Please enter a positive integer!");
                }
                return Integer.parseInt(input);
            } catch (InvalidNPostException e) {
                System.out.println(e.getMessage());
            }

        }

   }
}

