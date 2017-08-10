package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.model.BaseError;
import gov.nsf.common.util.NsfValidationUtils;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.request.SendLetterRequest;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacklinden on 1/3/17.
 */
public class SendLetterValidator{

    /**
     * Checks LetterRequest object fields for validity. If any invalid data is found,
     * an error List will be populated and a FormValidationException will be thrown
     * and caught by the BaseController
     *
     * @param letterRequest
     * @throws FormValidationException
     */
    public void validateRequest(SendLetterRequest letterRequest) throws FormValidationException {

        List<BaseError> errors = new ArrayList<BaseError>();

        errors.addAll(validateSendLetterRequest(letterRequest));

        if (!errors.isEmpty()) {
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
        return StringUtils.isEmpty(field);
    }

    /**
     * Checks emailRequest object fields for validity. If any invalid data is found,
     * an error List will be populated and a FormValidationException will be thrown
     * and caught by the BaseController
     *
     * @param letterRequest
     * @return List<BaseError>
     * @throws FormValidationException
     */

    public List<BaseError> validateSendLetterRequest(SendLetterRequest letterRequest) throws FormValidationException {
        List<BaseError> errors = new ArrayList<BaseError>();

        //emailRequest must not be null
        if (letterRequest == null) {
            errors.add(new BaseError(Constants.LETTER_REQUEST_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.LETTER_REQUEST_FIELD));
        } else if(letterRequest.getLetter() == null){
            errors.add(new BaseError(Constants.LETTER_FIELD, Constants.MISSING_LETTER));
        } else if(letterRequest.getLetter().getEmailInfo() == null ){
            errors.add(new BaseError(Constants.EMAIL_INFO_FIELD, Constants.MISSING_EMAIL_INFO));
        } else {
            Letter letter = letterRequest.getLetter();

            //emailRequest.body must be non-null/empty
            if (isInvalidString(letter.getEltrContent())) {
                errors.add(new BaseError(Constants.MAIL_BODY_EMAIL_FIELD, Constants.MISSING_EMAIL_BODY));
            }
            //emailRequest.mailSubject must be non-null/empty
            if (isInvalidString(letter.getEmailInfo().getMailSubject())) {
                errors.add(new BaseError(Constants.MAIL_SUBJECT_EMAIL_FIELD, Constants.MISSING_EMAIL_SUBJECT));
            }
            //emailRequest.fromAddress must be non-null/empty
            if (isInvalidString(letter.getEmailInfo().getFromAddress())) {
                errors.add(new BaseError(Constants.SENDER_EMAIL_ADDRESS_FIELD, Constants.MISSING_EMAIL_SENDER_ADDRESS));
            } else if (!NsfValidationUtils.isValidEmailAddress(letter.getEmailInfo().getFromAddress())) {
                errors.add(new BaseError(Constants.SENDER_EMAIL_ADDRESS_FIELD, Constants.INVALID_ADDRESS_ERROR));
            }

            // We only require that a to field is present.
            if (letter.getEmailInfo().getToAddresses().isEmpty()) {
                errors.add(new BaseError(Constants.TO_EMAIL_RECIPIENTS_FIELD, Constants.EMPTY_TO_EMAIL_RECIPIENTS));
            }

            validateAddress(errors, Constants.TO_EMAIL_RECIPIENTS_FIELD, letter.getEmailInfo().getToAddresses());
            validateAddress(errors, Constants.CC_EMAIL_RECIPIENTS_FIELD, letter.getEmailInfo().getCcAddresses());
            validateAddress(errors, Constants.BCC_EMAIL_RECIPIENTS_FIELD, letter.getEmailInfo().getBccAddresses());

            if (letterRequest.getSendMetaData() != null) {
                validateAddress(errors, Constants.META_DEBUG_EMAIL_RECIPIENTS_FIELD, letterRequest.getSendMetaData().getDebugRecipients());
                validateAddress(errors, Constants.META_DEFAULT_BCC_EMAIL_RECIPIENTS_FIELD, letterRequest.getSendMetaData().getDefaultBccRecipients());
                validateAddress(errors, Constants.META_PROD_SUPPORT_EMAIL_RECIPIENTS_FIELD, letterRequest.getSendMetaData().getProdSupportRecipients());
            }
        }

        return errors;
    }

    /**
     * Validates the address List
     *
     * @param errors
     * @param fieldName
     * @param recipList
     */
    private static void validateAddress(List<BaseError> errors, String fieldName, List<String> recipList) {

        // we are only checking items that exist.
        if (recipList == null || recipList.isEmpty()){
            return;
        }

        if (hasEmptyStringInList(recipList)) {
            errors.add(new BaseError(fieldName, Constants.EMPTY_STRING_EMAIL_RECIPIENTS));
            return; // break out early so we only show one error.
        }

        for (String emailAddress : recipList) {
            if (!NsfValidationUtils.isValidEmailAddress(emailAddress)) {
                errors.add(new BaseError(fieldName, Constants.INVALID_ADDRESS_ERROR));
                break;
            }
        }
    }

    /**
     * Determines if an empty string exists in the List
     *
     * @param strList
     * @return
     */
    private static boolean hasEmptyStringInList(List<String> strList) {
        if (strList != null) {
            for (String str : strList) {
                if (StringUtils.isEmpty(str)) {
                    return true;
                }
            }
        }
        return false;
    }
}


