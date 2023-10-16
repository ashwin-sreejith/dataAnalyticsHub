package com.ashwin.dataanalyticshub.customexceptions;

// thrown when post ID is invalid
public class InvalidPostIdException extends Exception {
    public InvalidPostIdException(String message){
        super(message);
    }
}
