package com.ashwin.dataanalyticshub.customexceptions;

// thrown when datetime format is invalid
public class InvalidDateTimeException extends Exception{
    public InvalidDateTimeException(String message){
        super(message);
    }
}
