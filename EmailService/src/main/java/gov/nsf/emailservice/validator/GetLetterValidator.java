package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.emailservice.api.model.Letter;

/**
 * EmailService validator for the GET route
 */
public class GetLetterValidator extends EmailServiceValidator {

    /**
     * Validates the request
     *
     * @param letter
     * @throws FormValidationException
     */
    @Override
    public void validateRequest(Letter letter) throws FormValidationException {
        validateRequest(letter.getEltrID());
    }

}
