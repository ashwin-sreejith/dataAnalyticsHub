package com.ashwin.dataanalyticshub.datamodel.customexceptions;

// thrown when duplicate post IDs are found
public class PostIdAlreadyExistsException extends Exception{
    public PostIdAlreadyExistsException(String message){
        super(message);
    }
}
