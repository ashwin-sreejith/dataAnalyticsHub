package com.ashwin.dataanalyticshub.datamodel.customexceptions;

// thrown when menu option picked by user is invalid
public class InvalidMenuEntryException extends Exception{
    public InvalidMenuEntryException(String message){
        super(message);;
    }
}
