package gov.nsf.emailservice.dao;

import gov.nsf.common.exception.ResourceNotFoundException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.*;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.emailservice.dao.rowmapper.LetterResultsSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import gov.nsf.emailservice.dao.rowmapper.SearchParameterResultSetExtractor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * EmailDaoImpl implements the EmailDao methods for retrieving information from
 * the DB
 *
 */
public class EmailDaoImpl implements EmailDao {

    @Autowired
    private DataSource dataSource;
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = Logger.getLogger(EmailDaoImpl.class);

    /**
     * DataSource getter
     *
     * @return DataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * DataSource setter
     *
     * @param dataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.setJdbcTemplate(new NamedParameterJdbcTemplate(dataSource));
    }

    /**
     * NamedParameterJdbcTemplate getter
     *
     * @return NamedParameterJdbcTemplate
     */
    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    /**
     * NamedParameterJdbcTemplate setter
     *
     * @param jdbcTemplate
     */
    public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Gets the letter by querying the eltr_eltr table using the eltrID as the key
     *
     * @param id
     * @return letter
     */
    @Override
    public Letter getLetter(String id) throws RollbackException {
        Letter letter = null;
        Map parameters = Collections.singletonMap(Constants.ELTR_ID, Integer.parseInt(id));

        try {
            letter = executeQuery(Constants.GET_LETTER_BY_ID_QUERY, parameters);
        } catch (Exception e) {
            LOGGER.error(Constants.ERROR_GETTING_LETTER + e);
            throw new RollbackException(Constants.ERROR_GETTING_LETTER + e);
        }

        if (letter == null) {
            LOGGER.error(Constants.ERROR_GETTING_LETTER + Constants.ERROR_LETTER_DOES_NOT_EXIST + id);
            throw new ResourceNotFoundException(Constants.ERROR_LETTER_DOES_NOT_EXIST + id);
        }

        List<SearchParameter> searchParameters = null;

        try{
            searchParameters = getSearchParameters(letter.getEltrID());
        } catch( RollbackException ex ){
            LOGGER.error(Constants.ERROR_GETTING_LETTER + ex);
            throw new ResourceNotFoundException(Constants.ERROR_GETTING_LETTER + ex);
        }

        letter.setSearchParameters(searchParameters);

        return letter;
    }

    /**
     * Finds all letters associated with the given search parameter key-value pair
     *
     * @param searchParameter
     * @return List<Letter>
     * @throws RollbackException
     */
    @Override
    public List<Letter> findLetter(SearchParameter searchParameter, Set<String> validSearchParameters) throws RollbackException{
        if(!validSearchParameters.contains(searchParameter.getKey())){
            throw new InvalidSearchParameterException(Constants.ERROR_FINDING_LETTER + Constants.SEARCH_PARAMETER_DOES_NOT_EXIST + searchParameter.getKey());
        }

        List<String> eltrIDs = null;
        try {
            eltrIDs = getLetterIDs(searchParameter);
        } catch(RollbackException ex){
            throw new RollbackException(Constants.ERROR_FINDING_LETTER + ex);
        }

        List<Letter> letters = new ArrayList<Letter>();

        for(String eltrID : eltrIDs ){
            try{
                letters.add(getLetter(eltrID));
            } catch( RollbackException ex){
                throw new RollbackException(Constants.ERROR_FINDING_LETTER + ex);
            }
        }

        return letters;
    }


    /**
     * Saves the letter into the ELTR DB
     * - Inserts letter into eltr_eltr table
     * - Inserts a row into eltr_eltr_mail_addr for the sender and each recipient (TO,CC,BCC)
     * - Inserts 
     * - Retrieves the recently inserted Letter
     *
     * @param letter
     * @return Letter
     * @throws RollbackException
     */
    @Override
    public Letter saveLetter(Letter letter, String templateID) throws RollbackException {

        try {
            insertEltr(letter,templateID);
            insertMailRecipients(letter.getEmailInfo(), Constants.MAX_ID);
        } catch (Exception e) {
            LOGGER.error(Constants.ERROR_SAVING_LETTER + e);
            throw new RollbackException(Constants.ERROR_SAVING_LETTER + e);
        }

        Letter storedLetter = null;

        try {
            storedLetter = executeQuery(Constants.GET_LETTER_BY_LAST_INSERTED_QUERY, Collections.emptyMap());
        } catch( RollbackException e){
            LOGGER.error(Constants.ERROR_SAVING_LETTER + Constants.ERROR_GETTING_UPDATED_LETTER + e);
            throw new RollbackException(Constants.ERROR_SAVING_LETTER + Constants.ERROR_GETTING_UPDATED_LETTER + e);
        }

        if( storedLetter == null ) {
            LOGGER.error(Constants.ERROR_SAVING_LETTER + Constants.ERROR_GETTING_SAVED_LETTER);
            throw new RollbackException(Constants.ERROR_SAVING_LETTER + Constants.ERROR_GETTING_SAVED_LETTER);
        }

        return storedLetter;

    }

    /**
     * Updates the letter into the ELTR DB
     * - Checks to see if the letter already exists
     * - If it doesn't, throws an exception (ending the transaction)
     * - Runs an update query
     *
     * @param letter
     * @return letter
     * @throws RollbackException
     */
    @Override
    public Letter updateLetter(Letter letter) throws RollbackException {
        Letter storedLetter = null;
        try {
            storedLetter = getLetter(letter.getEltrID());
        } catch (ResourceNotFoundException e) {
            LOGGER.error(Constants.ERROR_UPDATING_LETTER + e);
            throw new ResourceNotFoundException(Constants.ERROR_LETTER_DOES_NOT_EXIST + letter.getEltrID());
        } catch( Exception e ){
            throw new RollbackException(Constants.ERROR_UPDATING_LETTER + e);
        }

        if (storedLetter.getEltrStatus() == LetterStatus.Sent) {
            LOGGER.error(Constants.ERROR_UPDATING_LETTER + Constants.ERROR_LETTER_ALREADY_SENT_UPDATE + letter.getEltrID());
            throw new LetterAlreadySentException(Constants.ERROR_LETTER_ALREADY_SENT_UPDATE + letter.getEltrID());
        }

        boolean isNewStatus = letter.getEltrStatus() != storedLetter.getEltrStatus();
        try {
            updateEltr(letter, isNewStatus);
            deleteMailRecipients(letter.getEltrID());
            insertMailRecipients(letter.getEmailInfo(), letter.getEltrID());
        } catch (Exception e) {
            LOGGER.error(Constants.ERROR_UPDATING_LETTER + e);
            throw new RollbackException(Constants.ERROR_UPDATING_LETTER + e);
        }

        try {
            storedLetter = getLetter(letter.getEltrID());
        } catch( Exception e ){
            LOGGER.error(Constants.ERROR_UPDATING_LETTER + Constants.ERROR_GETTING_UPDATED_LETTER + e);
            throw new RollbackException(Constants.ERROR_UPDATING_LETTER + Constants.ERROR_GETTING_UPDATED_LETTER + e);
        }

        return storedLetter;
    }

    /**
     * Deletes the letter from the ltr DB
     * - Checks to see if the letter with passed ID exists
     * - Deletes rows from ntfy_ltr_addl_info (search parameters) with given eltrID
     * - Deletes rows from ntfy_ltr_mail_rcpt (mail recipients) with given eltrID
     * - Deletes row from ntfy_ltr (letter) with given eltrID
     *
     * @param id
     * @return
     * @throws RollbackException
     */
    @Override
    public Letter deleteLetter(String id) throws RollbackException {
        Letter storedLetter = null;
        try {
            storedLetter = getLetter(id);
        } catch (ResourceNotFoundException e) {
            LOGGER.error(Constants.ERROR_DELETING_LETTER + e);
            throw new ResourceNotFoundException(Constants.ERROR_LETTER_DOES_NOT_EXIST + id);
        } catch( Exception e ){
            throw new RollbackException(Constants.ERROR_DELETING_LETTER + e);
        }

        if (storedLetter.getEltrStatus() == LetterStatus.Sent) {
            LOGGER.error(Constants.ERROR_DELETING_LETTER + Constants.ERROR_LETTER_ALREADY_SENT_DELETE + id);
            throw new LetterAlreadySentException(Constants.ERROR_LETTER_ALREADY_SENT_DELETE + id);
        }

        try {
            deleteSearchParameters(id);
            deleteMailRecipients(id);
            deleteEltr(id);
        } catch(Exception ex){
            LOGGER.error(Constants.ERROR_DELETING_LETTER + ex);
            throw new RollbackException(Constants.ERROR_DELETING_LETTER + ex);
        }

        return storedLetter;
    }


    /**
     * Gets the search parameters for a given eltrID
     *
     * @param eltrID
     * @return List<SearchParameter>
     * @throws RollbackException
     */
    protected List<SearchParameter> getSearchParameters(String eltrID) throws RollbackException {

        List<SearchParameter> searchParameters = null;

        try{
            searchParameters = this.jdbcTemplate.query(Constants.GET_SEARCH_PARAMETERS_QUERY, Collections.singletonMap(Constants.ELTR_ID, Integer.parseInt(eltrID)), new SearchParameterResultSetExtractor());
        } catch( Exception ex ){
            throw new RollbackException(Constants.ERROR_RETRIEVING_SEARCH_PARAMETERS + eltrID + ": " + ex);
        }

        return searchParameters;
    }

    /**
     * Inserts the Letter's searchParameters into the DB
     *
     * @param letter
     * @throws RollbackException
     */
    @Override
    public void storeSearchParameters(Letter letter, Set<String> validSearchParameters) throws RollbackException {
        deleteSearchParameters(letter.getEltrID());

        Map<String, SearchParameter> insertedSearchParameters = new HashMap<String, SearchParameter>();
        for( int i = letter.getSearchParameters().size() - 1;  i >= 0; i --){
            SearchParameter searchParameter = letter.getSearchParameters().get(i);
            if(insertedSearchParameters.containsKey(searchParameter.getKey())){
                continue;
            }
            if(!validSearchParameters.contains(searchParameter.getKey())){
                throw new InvalidSearchParameterException(Constants.SEARCH_PARAMETER_DOES_NOT_EXIST + searchParameter.getKey());
            }
            try{
                executeUpdate(Constants.INSERT_SEARCH_PARAMETER_QUERY, getInsertSearchParameterParameters(letter, searchParameter));
            } catch( RollbackException ex ){
                throw new RollbackException(Constants.ERROR_INSERTING_SEARCH_PARAMETER + ex);
            }
            searchParameter.setId(letter.getEltrID() + Constants.SEPARATOR + searchParameter.getKey());
            insertedSearchParameters.put(searchParameter.getKey(), searchParameter);
        }

        letter.setSearchParameters(new ArrayList<SearchParameter>(insertedSearchParameters.values()));
    }

    /**
     * Returns Set of search parameter keys stored in lkup table
     *
     * @return
     * @throws RollbackException
     */
    @Override
    @Cacheable(value="searchParameterNames")
    public Set<String> getSearchParameterNames() throws RollbackException {
        Set<String> validSearchParameterNames = null;

        try{
            validSearchParameterNames = (Set<String>)this.jdbcTemplate.query(Constants.SEARCH_PARAMETER_LOOKUP_QUERY, Collections.emptyMap(), new ResultSetExtractor<Set<String>>() {
                @Override
                public Set<String> extractData(ResultSet resultSet) throws SQLException {
                    Set<String> paramNames = new HashSet<String>();
                    while(resultSet.next()){
                        paramNames.add(resultSet.getString(Constants.ATR_NAME) != null ? resultSet.getString(Constants.ATR_NAME) : "");
                    }
                    return paramNames;
                }
            });
        } catch (Exception ex) {
            throw new RollbackException(Constants.ERROR_LOOKING_UP_PARAMETER_NAMES + ex);
        }
        return validSearchParameterNames;
    }

    protected void deleteSearchParameters(String eltrID) throws RollbackException {
        Map parameters = Collections.singletonMap(Constants.ELTR_ID, Integer.parseInt(eltrID));

        try {
            executeUpdate(Constants.DELETE_SEARCH_PARAMETERS_QUERY, parameters);
        } catch (Exception e) {
            LOGGER.error(Constants.ERROR_DELETING_SEARCH_PARAMETERS + eltrID + ": " + e);
            throw new RollbackException(Constants.ERROR_DELETING_SEARCH_PARAMETERS + eltrID + ": " + e);
        }
    }

    /**
     * Returns a List of all eltrIDs for a given search parameter
     *
     * @param searchParameter
     * @return List<String>
     * @throws RollbackException
     */
    protected List<String> getLetterIDs(SearchParameter searchParameter) throws RollbackException{

        List<String> eltrIDs = null;
        Map parameters = getGetLetterIdsParameters(searchParameter);
        try{
            eltrIDs = this.jdbcTemplate.query(Constants.GET_LETTER_IDS_QUERY, parameters, new ResultSetExtractor<List<String>>(){

                @Override
                public List<String> extractData(ResultSet rs) throws SQLException {
                    List<String> ids = new ArrayList<String>();
                    while(rs.next()){
                        ids.add(rs.getString(Constants.ELTR_ID));
                    }
                    return ids;
                }
            });
        } catch (Exception e){
            throw new RollbackException(Constants.ERROR_GETTING_LETTER_IDS + searchParameter.getKey() + ": " + e);
        }

        return eltrIDs;
    }

    /**
     * Inserts into eltr_eltr_mail_addr for sender and each recipient email addresss
     *
     * @param emailInfo
     */
    protected void insertMailRecipients(EmailInfo emailInfo, String eltrID) throws RollbackException {

        try {
            insertMailRecipient(eltrID, emailInfo.getFromAddress(), Constants.FROM);

            for (String address : emailInfo.getToAddresses()) {
                insertMailRecipient(eltrID, address, Constants.TO);
            }

            for (String address : emailInfo.getCcAddresses()) {
                insertMailRecipient(eltrID, address, Constants.CC);
            }

            for (String address : emailInfo.getBccAddresses()) {
                insertMailRecipient(eltrID, address, Constants.BCC);
            }
        } catch (Exception e) {
            throw new RollbackException(Constants.ERROR_INSERTING_MAIL_RECIPIENT + e);
        }

    }

    /**
     * Inserts letter into ntfy_ltr table
     *
     * @param letter Letter object
     * @throws Exception - will be a Runtime Exception from DB
     */
    protected void insertEltr(Letter letter, String templateID) throws RollbackException{

        Map parameters = getInsertEltrParameters(letter, templateID);
        try{
            executeUpdate(Constants.INSERT_ELTR_QUERY, parameters);
        } catch( Exception e ){
            throw new RollbackException(Constants.ERROR_UPDATING_ELTR_ELTR_TABLE + e);
        }
    }

    /**
     * Deletes letter from the ntfy_ltr table
     * @param id
     * @throws RollbackException
     */
    private void deleteEltr(String id) throws RollbackException{
        Map parameters = Collections.singletonMap(Constants.ELTR_ID, Integer.parseInt(id));
        try{
            executeUpdate(Constants.DELETE_ELTR_QUERY, parameters);
        } catch( Exception e ){
            throw new RollbackException(Constants.ERROR_DELETING_ELTR + id +": " + e);
        }
    }


    /**
     * Updates the ntfy_ltr table
     *
     * @param letter
     */
    protected void updateEltr(Letter letter, boolean isNewStatus) throws RollbackException{
        String queryStr = Constants.UPDATE_LETTER_WITH_SAME_STATUS_QUERY;
        Map<String, String> parameters = getUpdateLetterParameters(letter);

        if( isNewStatus ){
            queryStr = Constants.UPDATE_LETTER_WITH_NEW_STATUS_QUERY;
            parameters.put(Constants.ELTR_STATUS, letter.getEltrStatus().getCode());
            parameters.put(Constants.ELTR_USER, letter.getEltrStatusUser());
        }

        try {
            executeUpdate(queryStr, parameters);
        } catch( Exception e ){
            throw new RollbackException(Constants.ERROR_UPDATING_ELTR_ELTR_TABLE + e);
        }
    }

    /**
     * Inserts into ntfy_ltr_mail_rcpt table
     *
     * @param mailAddress
     * @param recipientTypeCode
     *
     * @throws Exception - will be a Runtime Exception from DB
     */
    protected void insertMailRecipient(String eltrID, String mailAddress, String recipientTypeCode) throws RollbackException{

        String queryStr = null;
        Map parameters = getInsertMailRecipientParameters(mailAddress, recipientTypeCode);
        if (eltrID.equals(Constants.MAX_ID)) {
            queryStr = Constants.INSERT_MAIL_RECIPIENT_QUERY_WITHOUT_ID;
        } else {
            queryStr = Constants.INSERT_MAIL_RECIPIENT_QUERY_WITH_ID;
            parameters.put(Constants.ELTR_ID, Integer.parseInt(eltrID));
        }

        executeUpdate(queryStr, parameters);
    }

    /**
     * Removes all rows with the given eltrID in the ntfy_ltr_mail_rcpt table
     *
     * @param eltrID
     */
    private void deleteMailRecipients(String eltrID) throws RollbackException {
        Map parameters = Collections.singletonMap(Constants.ELTR_ID, Integer.parseInt(eltrID));

        try {
            executeUpdate(Constants.DELETE_MAIL_RECIPIENTS_QUERY, parameters);
        } catch (Exception e) {
            LOGGER.error(Constants.ERROR_DELETING_MAIL_RECIPIENTS + eltrID + e);
            throw new RollbackException(Constants.ERROR_DELETING_MAIL_RECIPIENTS + eltrID + e);
        }

    }


    /**
     * Helper method for executing jdbcTemplate.query(queryString,parameters,rowmapper/extractor) calls
     *
     * @param queryStr - the query string to use
     * @param parameters - the parameters to inject
     * @throws RollbackException
     */
    private Letter executeQuery(String queryStr, Map parameters) throws RollbackException {
        Letter letter = null;
        try {
            letter = this.jdbcTemplate.query(queryStr, parameters, new LetterResultsSetExtractor());
        } catch (Exception e) {
            throw new RollbackException(Constants.ERROR_EXECUTING_QUERY + queryStr + e);
        }

        return letter;
    }

    /**
     * Helper method for executing jdbcTemplate.update(queryString,parameters) calls
     *
     * @param queryStr - the query string to use
     * @param parameters - the parameters to inject
     * @throws RollbackException
     */
    private void executeUpdate(String queryStr, Map parameters) throws RollbackException {
        try {
            this.jdbcTemplate.update(queryStr, parameters);
        } catch (Exception e) {
            throw new RollbackException(Constants.ERROR_EXECUTING_QUERY + queryStr + e);
        }
    }

    /**
     * Returns the parameters necessary for the exec_request_table insert
     *
     * @param letter
     * @return Map of parameters
     */
    private static Map getInsertEltrParameters(Letter letter, String templateID) {
        Map parameters = new HashMap();

        parameters.put(Constants.ELTR_CONTENT, letter.getEltrContent());
        parameters.put(Constants.MAIL_SUBJECT, letter.getEmailInfo().getMailSubject());
        parameters.put(Constants.ELTR_STATUS, letter.getEltrStatus().getCode());
        parameters.put(Constants.ELTR_USER, letter.getEltrStatusUser());
        parameters.put(Constants.TEMPLATE_ID, (templateID != null && !templateID.isEmpty()) ? Integer.parseInt(templateID) : null);
        parameters.put(Constants.APPL_ID, Integer.parseInt(letter.getApplID()));
        parameters.put(Constants.LAST_UPT_PGM, Constants.EN_SVC);
        parameters.put(Constants.LAST_UPT_USER, letter.getEltrStatusUser());
        parameters.put(Constants.PLAIN_TEXT, Utils.convertBooleanToString(letter.isPlainText()));
        return parameters;
    }

    /**
     * Returns the parameters necessary for the eltr_eltr_mail_addr insert
     *
     * @param mailAddress
     * @param recipientTypeCode
     * @return
     */
    private static Map getInsertMailRecipientParameters(String mailAddress, String recipientTypeCode) {
        Map parameters = new HashMap();
        parameters.put(Constants.RECIPIENT_TYPE_CODE, recipientTypeCode);
        parameters.put(Constants.MAIL_ADDRESS, mailAddress);
        parameters.put(Constants.LAST_UPT_PGM, Constants.EN_SVC);
        parameters.put(Constants.LAST_UPT_USER, Constants.EN_SVC);

        return parameters;
    }

    /**
     * Returns the parameter necessary for the update letter query
     *
     * @param letter
     * @return
     */
    private static Map getUpdateLetterParameters(Letter letter) {
        Map parameters = new HashMap();
        parameters.put(Constants.ELTR_ID, Integer.parseInt(letter.getEltrID()));
        parameters.put(Constants.ELTR_CONTENT, letter.getEltrContent());
        parameters.put(Constants.MAIL_SUBJECT, letter.getEmailInfo().getMailSubject());
        parameters.put(Constants.LAST_UPT_PGM, Constants.EN_SVC);
        parameters.put(Constants.LAST_UPT_USER, letter.getEltrStatusUser());

        return parameters;
    }

    /**
     * Returns the parameter map necessary for the insert search parameter query
     *
     * @param letter
     * @param parameter
     * @return
     */
    private static Map getInsertSearchParameterParameters(Letter letter, SearchParameter parameter){
        Map parameters = new HashMap();
        parameters.put(Constants.ELTR_ID, Integer.parseInt(letter.getEltrID()));
        parameters.put(Constants.SEARCH_PARAMETER_KEY, parameter.getKey());
        parameters.put(Constants.SEARCH_PARAMETER_VALUE, parameter.getValue());
        parameters.put(Constants.LAST_UPT_USER, letter.getEltrStatusUser());
        parameters.put(Constants.LAST_UPT_PGM, Constants.EN_SVC);

        return parameters;
    }

    /**
     * Returns the parameter map necessary for the get letter IDs query
     *
     * @param searchParameter
     * @return
     */
    private static Map getGetLetterIdsParameters(SearchParameter searchParameter){
        Map parameters = new HashMap();
        parameters.put(Constants.ATR_NAME, searchParameter.getKey());
        parameters.put(Constants.SEARCH_PARAMETER_VALUE, searchParameter.getValue());
        return parameters;
    }


}
