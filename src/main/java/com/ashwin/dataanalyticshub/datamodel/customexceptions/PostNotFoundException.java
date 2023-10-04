package com.ashwin.dataanalyticshub.datamodel.customexceptions;

// thrown when post being queried doesn't exist
public class PostNotFoundException extends Exception{
    public PostNotFoundException(String message){
        super(message);
    }
}
