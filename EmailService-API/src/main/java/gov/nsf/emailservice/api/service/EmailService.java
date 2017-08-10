package gov.nsf.emailservice.api.service;

import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendMetaData;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseResponseWrapper;

import gov.nsf.emailservice.api.model.*;

/**
 * EmailService interface
 *
 */
public interface EmailService {

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

    public LetterResponseWrapper getLetter(String id) throws RollbackException;

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
    public LetterListResponseWrapper findLetter(SearchParameter searchParameter) throws RollbackException;

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
    public LetterResponseWrapper saveLetter(Letter letter) throws RollbackException;

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
    public LetterResponseWrapper saveLetter(Letter letter, String templateID) throws RollbackException;


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
     *  LetterStatus.Sent constraints
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

    public LetterResponseWrapper updateLetter(Letter letter) throws RollbackException;

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
    public LetterResponseWrapper deleteLetter(String id) throws RollbackException;

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
    public SearchParameterResponseWrapper getSearchParameters(String id) throws RollbackException;

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
    public BaseResponseWrapper sendLetter(Letter letter, SendMetaData metaData);


}
