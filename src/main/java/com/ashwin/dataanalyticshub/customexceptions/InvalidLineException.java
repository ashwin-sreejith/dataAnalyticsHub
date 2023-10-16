package com.ashwin.dataanalyticshub.customexceptions;

// thrown when a line read from file is invalid
public class InvalidLineException extends Exception{
    public InvalidLineException(String message){
        super(message);
    }
}
