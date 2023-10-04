package com.ashwin.dataanalyticshub.datamodel.customexceptions;

// thrown when post collection is empty
public class PostCollectionEmptyException extends Exception{
    public PostCollectionEmptyException(String message){
        super(message);
    }
}
