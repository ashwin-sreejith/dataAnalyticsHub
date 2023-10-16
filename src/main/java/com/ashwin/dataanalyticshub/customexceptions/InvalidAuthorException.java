package com.ashwin.dataanalyticshub.customexceptions;

// thrown when author entry is invalid
public class InvalidAuthorException extends Exception{
    public InvalidAuthorException(String message){
        super(message);
    }
}
