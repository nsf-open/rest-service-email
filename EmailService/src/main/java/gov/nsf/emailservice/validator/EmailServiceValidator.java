package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.model.BaseError;
import gov.nsf.common.util.NsfValidationUtils;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.common.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EmailServiceValidator class to be inherited by validators
 *
 */
public abstract class EmailServiceValidator {
    private static final Logger LOGGER = Logger.getLogger(EmailServiceValidator.class);

    /**
     * Validate Request method to be implemented by subclasses
     *
     * @param letter
     * @throws FormValidationException
     */
    public abstract void validateRequest(Letter letter) throws FormValidationException;

    /**
     * Validates the eltrID
     *
     * @param eltrID
     * @throws FormValidationException
     */
    public void validateRequest(String eltrID) throws FormValidationException {
        List<BaseError> errors = new ArrayList<BaseError>();

        if( isInvalidString(eltrID) ){
            errors.add(new BaseError(Constants.ELTR_ID_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_ID_FIELD));
        } else if( isInvalidIdNumberString(eltrID)){
            errors.add(new BaseError(Constants.ELTR_ID_FIELD, Constants.ELTR_ID_NON_NUMERIC));
        }

        if (errorOccurred(errors)) {
            throw new FormValidationException(Constants.INVALID_FORM_DATA, errors);
        }

    }
    /**
     * Check if the passed string is null or empty (i.e. "") or contains illegal characters
     *
     * @param field
     * @return true/false
     */
    protected boolean isInvalidString(String field) {
        String trimmedField = field != null ? field.trim() : "";
        return StringUtils.isEmpty(trimmedField) ;

    }


    /**
     * Checks to see if String is an Integer
     *
     * @param id
     * @return
     */
    protected boolean isInvalidIdNumberString(String id){
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            LOGGER.error(e);
            return true;
        }

        return false;
    }

    /**
     * Checks for valid UTF8
     * @param input
     * @return
     */
    protected boolean isValidUTF8(String input){
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        CharsetEncoder encoder = Charset.forName("Windows-1252").newEncoder();

        ByteBuffer tmp;
        try {
            tmp = encoder.encode(CharBuffer.wrap(input));
        } catch(CharacterCodingException e) {
            LOGGER.error(e);
            return false;
        }

        try {
            decoder.decode(tmp);
            return true;
        }  catch(CharacterCodingException e){
            LOGGER.error(e);
            return false;
        }
    }

    /**
     * Validates the List of email address strings
     *
     * @param fieldName
     * @param recipientList
     * @return
     */
    protected List<BaseError> validateAddressList(String fieldName, List<String> recipientList){
        List<BaseError> errors = new ArrayList<BaseError>();

        if(recipientList == null ||recipientList.isEmpty()){
            return Collections.emptyList();
        }

        boolean invalidAddressOccured = false;

        for(String emailAddress : recipientList){
            if( invalidAddressOccured ){
                return errors;
            }
            if(!NsfValidationUtils.isValidEmailAddress(emailAddress)){
                errors.add(new BaseError(fieldName, Constants.INVALID_ADDRESS_FORMAT + fieldName));
                invalidAddressOccured = true;
            }
        }
        return errors;
    }

    /**
     * Returns true if an error is present in the list
     *
     * @param errors
     * @return
     */
    protected boolean errorOccurred(List<BaseError> errors){
        return !errors.isEmpty();
    }

    /**
     * Validates Letter.searchParameters fields
     *
     * @param searchParameters
     * @return List<BaseError>
     */
    protected List<BaseError> validateSearchParameters(List<SearchParameter> searchParameters) {
        //searchParameters must be non-null/non-empty
        if (searchParameters == null || searchParameters.isEmpty()) {
            return Collections.singletonList(new BaseError(Constants.SEARCH_PARAMETERS_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.SEARCH_PARAMETERS_FIELD));
        }

        List<BaseError> errors = new ArrayList<BaseError>();

        //SearchParameters must not contain any null/empty key or value fields
        for (SearchParameter parameter : searchParameters) {
            if (isInvalidString(parameter.getKey()) || isInvalidString(parameter.getValue())) {
                errors.add(new BaseError(Constants.SEARCH_PARAMETERS_FIELD, Constants.INVALID_PARAMETER_KEY_OR_VALUE));
                break;
            }
        }
        return errors;

    }
}
