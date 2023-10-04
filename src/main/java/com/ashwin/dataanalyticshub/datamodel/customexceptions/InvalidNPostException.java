package com.ashwin.dataanalyticshub.datamodel.customexceptions;

// thrown when top-N-posts chosen is invalid
public class InvalidNPostException extends Exception{
    public InvalidNPostException(String message){
        super(message);
    }
}
