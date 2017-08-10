package gov.nsf.emailservice.validator;


import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseError;
import gov.nsf.common.util.NsfValidationUtils;
import gov.nsf.emailservice.api.model.EmailInfo;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterStatus;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.referencedataservice.api.model.ApplicationInfo;
import gov.nsf.referencedataservice.api.service.ApplicationInfoService;
import gov.nsf.templateservice.model.LetterTemplateResponseWrapper;
import gov.nsf.templateservice.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CreateLetterValidator class
 *
 */
public class CreateLetterValidator extends EmailServiceValidator {

    @Autowired
    protected TemplateService templateServiceClient;

    @Autowired
    protected ApplicationInfoService applicationInfoServiceClient;

    /**
     * TemplateServiceClient getter
     * @return TemplateService
     */
    public TemplateService getTemplateServiceClient() {
        return templateServiceClient;
    }

    /**
     * TemplateServiceClient setter
     * @param templateServiceClient
     */
    public void setTemplateServiceClient(TemplateService templateServiceClient) {
        this.templateServiceClient = templateServiceClient;
    }


    /**
     * ApplicationInfoService getter
     * @return ApplicationInfoService
     */
    public ApplicationInfoService getApplicationInfoServiceClient() {
        return applicationInfoServiceClient;
    }

    /**
     * ApplicationInfoService setter
     * @param applicationInfoServiceClient
     */
    public void setApplicationInfoServiceClient(ApplicationInfoService applicationInfoServiceClient) {
        this.applicationInfoServiceClient = applicationInfoServiceClient;
    }


    /**

     * Checks Letter object fields for validity. If any invalid data is found,
     * an error List will be populated and a FormValidationException will be thrown
     * and caught by the BaseController
     *
     * @param letter
     * @throws FormValidationException
     */
    @Override
    public void validateRequest(Letter letter) throws FormValidationException {
        if (letter == null) {
            throw new FormValidationException(Constants.INVALID_FORM_DATA, Collections.singletonList(new BaseError(Constants.LETTER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.LETTER_FIELD)));
        }

        List<BaseError> errors = new ArrayList<BaseError>();

        errors.addAll(validateLetter(letter));
        errors.addAll(validateEmailInfo(letter.getEmailInfo(), letter.getEltrStatus()));
        errors.addAll(validateSearchParameters(letter.getSearchParameters()));
        errors.addAll(validateTemplate(letter.getTmplID()));

        if (errorOccurred(errors)) {
            throw new FormValidationException(Constants.INVALID_FORM_DATA, errors);
        }
    }


    /**
     * Validates top-level Letter fields
     *
     * @param letter
     * @return List<BaseError>
     */
    private List<BaseError> validateLetter(Letter letter) {
        List<BaseError> errors = new ArrayList<BaseError>();

        if( letter.getEltrStatus() == null ){
            errors.add(new BaseError(Constants.ELTR_STATUS_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_STATUS_FIELD));
            letter.setEltrStatus(LetterStatus.Draft);
        } else if (letter.getEltrStatus() == LetterStatus.Invalid) {
            errors.add(new BaseError(Constants.ELTR_STATUS_FIELD, Constants.INVALID_ELTR_STATUS));
            letter.setEltrStatus(LetterStatus.Draft);
        }

        if(isInvalidString(letter.getApplID())){
            errors.add(new BaseError(Constants.APPL_ID_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.APPL_ID_FIELD));
        } else if( isInvalidIdNumberString(letter.getApplID())){
            errors.add(new BaseError(Constants.APPL_ID_FIELD, Constants.APPL_ID_NON_NUMERIC));
        } else {
            ApplicationInfo applicationInfo = applicationInfoServiceClient.getApplicationInfo(letter.getApplID());
            if( applicationInfo == null ){
                errors.add(new BaseError(Constants.APPL_ID_FIELD, Constants.APPL_ID_DOES_NOT_EXIST + letter.getApplID()));
            }
        }

        if (letter.getEltrStatus() == LetterStatus.Draft && letter.getEltrContent() == null) {
            errors.add(new BaseError(Constants.ELTR_CONTENT_FIELD, Constants.MISSING_NULL_FIELD + Constants.ELTR_CONTENT_FIELD));
        } else if (letter.getEltrStatus() == LetterStatus.Sent && isInvalidString(letter.getEltrContent())) {
            errors.add(new BaseError(Constants.ELTR_CONTENT_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_CONTENT_FIELD));
        } else if (letter.getEltrContent() != null && !isValidUTF8(letter.getEltrContent())) {
            errors.add(new BaseError(Constants.ELTR_CONTENT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.ELTR_CONTENT_FIELD));
        }

        //EltrStatusUser must be non-null/empty
        if (isInvalidString(letter.getEltrStatusUser())) {
            errors.add(new BaseError(Constants.ELTR_USER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_USER_FIELD));
        }



        return errors;
    }

    /**
     * Validates Letter.emailInfo fields
     *
     * @param emailInfo
     * @param letterStatus
     * @return List<BaseError>
     */
    private List<BaseError> validateEmailInfo(EmailInfo emailInfo, LetterStatus letterStatus) {
        if (emailInfo == null) {
            return Collections.singletonList(new BaseError(Constants.EMAIL_INFO_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.EMAIL_INFO_FIELD));
        }

        List<BaseError> errors = new ArrayList<BaseError>();

        if (letterStatus == LetterStatus.Draft) {
            //EmailInfo.mailSubject must be non-null
            if (emailInfo.getMailSubject() == null) {
                errors.add(new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.MISSING_NULL_FIELD + Constants.MAIL_SUBJECT_FIELD));
            } else if (!isValidUTF8(emailInfo.getMailSubject())) { //EmailInfo.mailSubject must not contain illegal chars
                errors.add(new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.MAIL_SUBJECT_FIELD));
            }

            //EmailInfo.toAddresses must be non-null
            if (emailInfo.getToAddresses() == null) {
                errors.add(new BaseError(Constants.TO_RECIPIENTS_FIELD, Constants.MISSING_NULL_FIELD + Constants.TO_RECIPIENTS_FIELD));
            }
        }
        //Validation cases with LetterStatus == Sent
        else if (letterStatus == LetterStatus.Sent) {

            //EmailInfo.mailSubject must be non-null/non-empty
            if (isInvalidString(emailInfo.getMailSubject())) {
                errors.add(new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.MAIL_SUBJECT_FIELD));
            } else if (!isValidUTF8(emailInfo.getMailSubject())) { //EmailInfo.mailSubject must not contain illegal chars
                errors.add(new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.MAIL_SUBJECT_FIELD));
            }

            //EmailInfo.toAddresses must be non-null/non-empty
            if (emailInfo.getToAddresses() == null || emailInfo.getToAddresses().isEmpty()) {
                errors.add(new BaseError(Constants.TO_RECIPIENTS_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.TO_RECIPIENTS_FIELD));
            }
        }


        //EmailInfo.ccAddresses must be non-null
        if (emailInfo.getCcAddresses() == null) {
            errors.add(new BaseError(Constants.CC_RECIPIENTS_FIELD, Constants.MISSING_NULL_FIELD + Constants.CC_RECIPIENTS_FIELD));
        }

        //EmailInfo.bccAddresses must be non-null
        if (emailInfo.getBccAddresses() == null) {
            errors.add(new BaseError(Constants.BCC_RECIPIENTS_FIELD, Constants.MISSING_NULL_FIELD + Constants.BCC_RECIPIENTS_FIELD));
        }

        //EmailInfo.fromAddress must be non-null
        if( !NsfValidationUtils.isValidEmailAddress(emailInfo.getFromAddress()) ) {
            errors.add(new BaseError(Constants.SENDER_ADDRESS_FIELD, Constants.INVALID_ADDRESS_FORMAT + Constants.SENDER_ADDRESS_FIELD));
        } else if (isInvalidString(emailInfo.getFromAddress())) {
            errors.add(new BaseError(Constants.SENDER_ADDRESS_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.SENDER_ADDRESS_FIELD));
        }

        errors.addAll(validateAddressList(Constants.TO_RECIPIENTS_FIELD, emailInfo.getToAddresses()));
        errors.addAll(validateAddressList(Constants.CC_RECIPIENTS_FIELD, emailInfo.getCcAddresses()));
        errors.addAll(validateAddressList(Constants.BCC_RECIPIENTS_FIELD, emailInfo.getBccAddresses()));


        return errors;
    }

    /**
     * Validates template
     *
     * @param templateID
     * @return
     */
    private List<BaseError> validateTemplate(String templateID){
        //templateID is an optional URL parameter (if not passed, don't validate)
        if(templateID == null ){
            return Collections.emptyList();
        }
        List<BaseError> errors = new ArrayList<BaseError>();

        LetterTemplateResponseWrapper templateWrapper = null;
        try {
            templateWrapper = templateServiceClient.getLetterTemplate(templateID);

            if( !templateWrapper.getErrors().isEmpty() ){
                errors.addAll(templateWrapper.getErrors());
            }
        } catch ( RollbackException ex ){
            errors.add(new BaseError(Constants.TMPL_ID_FIELD, Constants.ERROR_LOOKING_UP_TEMPLATE + ": " + ex));
        }

        return errors;
    }

}
