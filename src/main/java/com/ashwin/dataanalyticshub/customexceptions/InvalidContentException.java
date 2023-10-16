package com.ashwin.dataanalyticshub.customexceptions;

// thrown when content entry is invalid
public class InvalidContentException extends Exception{
    public InvalidContentException(String message){
        super(message);
    }
}
