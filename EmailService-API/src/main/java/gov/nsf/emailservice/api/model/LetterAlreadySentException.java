package gov.nsf.emailservice.api.model;

import gov.nsf.common.exception.RollbackException;

/**
 * LetterAlreadySentException
 */
public class LetterAlreadySentException extends RollbackException{

    public LetterAlreadySentException(String msg){
        super(msg);
    }

    public LetterAlreadySentException(String msg, Throwable throwable){
        super(msg, throwable);
    }
}
