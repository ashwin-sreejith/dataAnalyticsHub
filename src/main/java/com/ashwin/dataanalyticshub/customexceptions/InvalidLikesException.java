package com.ashwin.dataanalyticshub.customexceptions;

// thrown when likes value is invalid
public class InvalidLikesException extends Exception{
    public InvalidLikesException(String message){
        super(message);
    }
}
