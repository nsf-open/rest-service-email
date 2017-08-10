package gov.nsf.emailservice.service;

import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendLevelEnum;
import gov.mynsf.common.email.model.SendMetaData;
import gov.mynsf.common.email.util.NsfEmailUtil;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseResponseWrapper;
import gov.nsf.common.util.NsfValidationUtils;
import gov.nsf.emailservice.api.model.*;
import gov.nsf.emailservice.api.service.EmailService;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.emailservice.dao.EmailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EmailServiceImpl implements the EmailService interface and is responsible for
 * calling the DAO
 *
 */
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailDao emailDao;
    @Autowired
    private NsfEmailUtil nsfEmailUtil;


    /**
     * EmailDao getter
     *
     * @return EmaiLDao
     */
    public EmailDao getEmailDao() {
        return emailDao;
    }

    /**
     * EmailDao setter
     */
    public void setEmailDao(EmailDao emailDao) {
        this.emailDao = emailDao;
    }

    /**
     * Returns the Letter stored in the ltr database for the passed Letter ID
     *
     * @param id
     *      - id must be non-null/non-empty and must be an integer string
     * @return LetterResponseWrapper
     *      - contains a Letter object
     *      - contains three Lists of errors, warnings, and informationals with BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class, RollbackException.class})
    public LetterResponseWrapper getLetter(String id) throws RollbackException {
        Letter returnedLetter = emailDao.getLetter(id);
        return new LetterResponseWrapper(returnedLetter);
    }

    /**
     * Returns all Letters stored in the ltr database with that are mapped to the passed search parameter
     *
     * @param searchParameter
     *      - searchParameter must be non-null
     *      - searchParameter.key must be non-null/non-empty and must be a valid search parameter key (e.g. Award_Id)
     *      - searchParameter.value must be non-null/non-empty
     * @return LetterListResponseWrapper
     *      - contains a List of Letter objects
     *      - contains three Lists of errors, warnings, and informationals with BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class, RollbackException.class})
    public LetterListResponseWrapper findLetter(SearchParameter searchParameter) throws RollbackException {
        List<Letter> letters = emailDao.findLetter(searchParameter, emailDao.getSearchParameterNames());
        return new LetterListResponseWrapper(letters);
    }

    /**
     *  Saves the passed Letter object into the ltr database and returns the stored Letter object
     *
     *  @param letter
     *  letter must be non-null and letter.letterStatus must be either LetterStatus.Draft or LetterStatus.Sent
     *
     *  LetterStatus.Draft constraints
     *      - letter.eltrContent must be non-null (but may be empty, i.e. "") and must not contain non-UTF8 characters
     *      - letter.eltrStatusUser must be non-null/non-empty
     *      - letter.applID must be non-null/non-empty and must be a valid application ID
     *      - letter.emailInfo.mailSubject must be non-null (but may be empty, i.e. "") and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.searchParameters must be non-null/non-empty and must contain a SearchParameter object with a valid key (e.g. Award_Id)
     *
     *  LetterStatus.Sent constraints
     *      - letter.eltrContent must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.eltrStatusUser must be non-null/non-empty
     *      - letter.applID must be non-null/non-empty and must be a valid application ID
     *      - letter.emailInfo.mailSubject must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.searchParameters must be non-null/non-empty and must contain a SearchParameter object with a valid key (e.g. Award_Id)
     *
     * @return LetterResponseWrapper
     *      - contains a Letter object
     *          - letter.eltrID will be updated to the auto-generated ID
     *          - letter.eltrStatusDate will be updated to the current time
     *      - contains three Lists of errors, warnings, and informational BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class, RollbackException.class})
    public LetterResponseWrapper saveLetter(Letter letter) throws RollbackException {
        return this.saveLetter(letter,null);
    }

    /**
     *  Saves the passed Letter object into the ltr database and returns the stored Letter object
     *
     *  Associates this Letter with the passed templateID
     *
     *  @param letter
     *  letter must be non-null and letter.letterStatus must be either LetterStatus.Draft or LetterStatus.Sent
     *
     *  LetterStatus.Draft constraints
     *      - letter.eltrContent must be non-null (but may be empty, i.e. "") and must not contain non-UTF8 characters
     *      - letter.eltrStatusUser must be non-null/non-empty
     *      - letter.applID must be non-null/non-empty and must be a valid application ID
     *      - letter.emailInfo.mailSubject must be non-null (but may be empty, i.e. "") and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.searchParameters must be non-null/non-empty and must contain a SearchParameter object with a valid key (e.g. Award_Id)
     *
     *  LetterStatus.Sent constraints
     *      - letter.eltrContent must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.eltrStatusUser must be non-null/non-empty
     *      - letter.applID must be non-null/non-empty and must be a valid application ID
     *      - letter.emailInfo.mailSubject must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.searchParameters must be non-null/non-empty and must contain a SearchParameter object with a valid key (e.g. Award_Id)
     *  @param templateID
     *      - templateID must not non-null/non-empty and must be a valid template ID
     *
     * @return LetterResponseWrapper
     *      - contains a Letter object
     *          - letter.eltrID will be updated to the auto-generated ID
     *          - letter.eltrStatusDate will be updated to the current time
     *      - contains three Lists of errors, warnings, and informational BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class, RollbackException.class})
    public LetterResponseWrapper saveLetter(Letter letter, String templateID) throws RollbackException {
        Letter returnedLetter = emailDao.saveLetter(letter,templateID);
        returnedLetter.setSearchParameters(letter.getSearchParameters());
        emailDao.storeSearchParameters(returnedLetter, emailDao.getSearchParameterNames());
        return new LetterResponseWrapper(returnedLetter);
    }

    /**
     *  Updates the passed Letter object into the ltr database and returns the updated Letter object
     *
     *  @param letter
     *  letter must be non-null and letter.letterStatus must be either LetterStatus.Draft or LetterStatus.Sent
     *
     *  NOTE: One cannot update a Letter that has already been Sent (i.e. already stored w/ LetterStatus.Sent)
     *
     *  LetterStatus.Draft constraints and updatable fields
     *      - letter.eltrContent must be non-null (but may be empty, i.e. "") and must not contain non-UTF8 characters
     *      - letter.emailInfo.mailSubject must be non-null (but may be empty, i.e. "") and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null (but may be empty, i.e. []) and must not contain an invalid email address
     *      - letter.searchParameters must be non-null/non-empty and must contain a SearchParameter object with a valid key (e.g. Award_Id)
     *
     *  LetterStatus.Sent constraints and updatable fields
     *      - letter.eltrContent must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.emailInfo.mailSubject must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.searchParameters must be non-null/non-empty and must contain a SearchParameter object with a valid key (e.g. Award_Id)
     *
     * @return LetterResponseWrapper
     *      - contains a Letter object
     *          - letter.eltrStatusDate will be updated if the value is different than the stored value
     *      - contains three Lists of errors, warnings, and informationals with BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {InvalidSearchParameterException.class, RuntimeException.class, RollbackException.class})
    public LetterResponseWrapper updateLetter(Letter letter) throws RollbackException {
        Letter returnedLetter = emailDao.updateLetter(letter);
        returnedLetter.setSearchParameters(letter.getSearchParameters());
        emailDao.storeSearchParameters(returnedLetter, emailDao.getSearchParameterNames());
        return new LetterResponseWrapper(returnedLetter);
    }

    /**
     * Deletes the Letter stored in the ltr database with the passed id
     *
     * @param id
     *      - id must be non-null/non-empty and must be an integer string
     *      - id must be a valid letter ID in the database
     * @return LetterResponseWrapper
     *      - contains a Letter object
     *      - contains three Lists of errors, warnings, and informationals with BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = {InvalidSearchParameterException.class, RuntimeException.class, RollbackException.class})
    public LetterResponseWrapper deleteLetter(String id) throws RollbackException {
        Letter deletedLetter = emailDao.deleteLetter(id);
        return new LetterResponseWrapper(deletedLetter);
    }

    /**
     * Returns the search parameters stored in the ltr database for the passed Letter ID
     *
     * @param id
     *      - id must be non-null/non-empty and must be an integer string
     *      - id must be a valid letter ID in the database
     * @return SearchParameterResponseWrapper
     *      - contains a List of Search Parameter objects
     *      - contains three Lists of errors, warnings, and informationals with BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     *
     * @throws RollbackException
     */
    @Override
    @Transactional(value = "flp", readOnly = true, propagation = Propagation.REQUIRES_NEW, rollbackFor = {RuntimeException.class, RollbackException.class})
    public SearchParameterResponseWrapper getSearchParameters(String id) throws RollbackException {
        Letter letter = emailDao.getLetter(id);
        return new SearchParameterResponseWrapper(letter.getSearchParameters());
    }


    /**
     * Sends an email with the information provided in the Letter object
     *
     * @param letter
     *      - letter.eltrContent must be non-null/non-empty string and must not contain non-UTF8 characters
     *      - letter.emailInfo.mailSubject must be non-null/non-empty and must not contain non-UTF8 characters
     *      - letter.emailInfo.fromAddress must be non-null and must be a valid email address format
     *      - letter.emailInfo.toAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.ccAddresses must be non-null/non-empty and must not contain an invalid email address
     *      - letter.emailInfo.bccAddresses must be non-null/non-empty and must not contain an invalid email address
     * @param metaData
     *    SendLevel.Debug
     *      - metaData.debugRecipients must be non-null/non-empty and must not contain invalid email addresses
     *          - emails will ONLY be sent to these addresses ( will not send emails to to/cc/bcc from the Letter object)
     *      - metaData.prodSupportRecipients is ignored for this send level
     *      - metaData.defaultBccRecipients is ignored for this send level
     *    SendLevel.Prod
     *      - metaData.debugRecipients is ignored for this send level
     *      - metaData.prodSupportRecipients will send an email to the addresses listed in this field should an error occur
     *      - metaData.defaultBccAddresses will send an email to the addresses listed here in addition to the to/cc/bcc addresses
     *          - "poor man's email storage"
     *
     *
     * @return BaseResponseWrapper
     *      - contains three Lists of errors, warnings, and informationals with BaseError objects
     *          - errors list will be populated if any validations fail or any exceptions are thrown
     */
    @Override
    public BaseResponseWrapper sendLetter(Letter letter, SendMetaData metaData) {
        EmailRequest emailRequest = Utils.convertLetterToEmailRequest(letter,metaData);

        if (!isValidDebugRecipients(emailRequest)) {
            throw new IllegalArgumentException("Debug Recipient invalid. SendMetaData must contain a valid debugRecipient(s) when sendLevel==[DebugLevel].");
        }
        nsfEmailUtil.sendEmail(emailRequest);

        return new BaseResponseWrapper();
    }

    /**
     * Helper method to determine if the debug recipient list is valid when SendLevel==Debug
     * @param emailRequest
     * @return true/false
     */
    protected boolean isValidDebugRecipients(EmailRequest emailRequest) {
        if (nsfEmailUtil.getSendLevel() != SendLevelEnum.DebugLevel) {
            return true;
        }
        if (emailRequest.getSendMetaData() == null ||
                emailRequest.getSendMetaData().getDebugRecipients() == null ||
                emailRequest.getSendMetaData().getDebugRecipients().isEmpty() ) {
            return false;
        }

        for (String emailAddress : emailRequest.getSendMetaData().getDebugRecipients()) {
            if (!NsfValidationUtils.isValidEmailAddress(emailAddress)) {
                return false;
            }
        }

        return true;
    }

    public void setNsfEmailUtil(NsfEmailUtil nsfEmailUtil) {
        this.nsfEmailUtil = nsfEmailUtil;
    }
}
