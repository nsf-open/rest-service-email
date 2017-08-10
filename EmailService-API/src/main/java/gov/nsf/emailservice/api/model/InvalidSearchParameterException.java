package gov.nsf.emailservice.api.model;

import gov.nsf.common.exception.ResourceNotFoundException;

/**
 * InvalidSearchParameterException for missing search parameters
 */
public class InvalidSearchParameterException extends ResourceNotFoundException {
    public InvalidSearchParameterException(String msg) {
        super(msg);
    }

    public InvalidSearchParameterException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
