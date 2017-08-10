package gov.nsf.emailservice.dao;

import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.SearchParameter;

import java.util.List;
import java.util.Set;

/**
 * EmailDao interface
 *
 */
public interface EmailDao {

    /**
     * Gets the letter by querying the eltr_eltr table using eltrID as the key
     *
     * @param id
     * @return letter
     */
    public Letter getLetter(String id) throws RollbackException;

    /**
     * Gets the letters by querying the based on the search parameter
     *
     * @param searchParameter
     * @return List<Letter>
     */
    public List<Letter> findLetter(SearchParameter searchParameter, Set<String> validSearchParameters) throws RollbackException;


    /**
     * Saves the letter into the ELTR DB
     *
     * - Inserts letter into eltr_eltr table
     * - Inserts a row into eltr_eltr_mail_addr for the sender and each recipient (TO,CC,BCC)
     *
     * @param letter
     * @return
     * @throws RollbackException
     */
    public Letter saveLetter(Letter letter, String templateID) throws RollbackException;


    /**
     * Updates the letter into the ELTR DB
     * - Checks to see if the letter already exists
     *   - If it doesn't, throws an exception (ending the transaction)
     *
     * @param letter
     * @return
     * @throws RollbackException
     */
    public Letter updateLetter(Letter letter) throws RollbackException;

    /**
     *
     * @param id
     */
    public Letter deleteLetter(String id) throws RollbackException;

    /**
     * Stores the search paremeters in the table
     *
     * @param letter
     * @param validSearchParameters
     * @throws RollbackException
     */
    public void storeSearchParameters(Letter letter, Set<String> validSearchParameters) throws RollbackException;

        /**
         * Returns a set of search parameter names from the lookup table
         *
         * @return Set<String>
         * @throws RollbackException
         */
    public Set<String> getSearchParameterNames() throws RollbackException;
}
