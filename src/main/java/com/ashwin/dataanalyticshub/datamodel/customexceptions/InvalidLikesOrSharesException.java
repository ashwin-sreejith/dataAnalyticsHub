package com.ashwin.dataanalyticshub.datamodel.customexceptions;

// thrown when number of likes or shares is invalid
public class InvalidLikesOrSharesException extends Exception{
    public InvalidLikesOrSharesException(String message){
        super(message);
    }
}
