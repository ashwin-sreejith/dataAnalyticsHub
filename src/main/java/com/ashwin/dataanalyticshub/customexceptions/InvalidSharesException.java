package com.ashwin.dataanalyticshub.customexceptions;

// thrown when number of shares is invalid
public class InvalidSharesException extends Exception{
    public InvalidSharesException(String message){
        super(message);
    }
}
