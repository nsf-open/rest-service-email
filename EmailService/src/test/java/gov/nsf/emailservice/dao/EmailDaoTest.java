package gov.nsf.emailservice.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import gov.nsf.common.exception.ResourceNotFoundException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.InvalidSearchParameterException;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;
import gov.nsf.emailservice.dao.rowmapper.LetterResultsSetExtractor;

import java.util.*;

import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterStatus;
import gov.nsf.emailservice.dao.rowmapper.SearchParameterResultSetExtractor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * EmailDao JUnit tests
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class EmailDaoTest {

    @InjectMocks
    private EmailDaoImpl emailDao;

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplateMock;


    @Before
    public void checkFieldsNotNull() {
        assertNotNull(emailDao);
        assertNotNull(emailDao.getJdbcTemplate());
    }

    /**
     * Tests that the normal behavior of the getLetter(int) method properly calls
     * the NamedParameterJdbcTemplate.query method to get the letter from the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully query
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getLetterByIdHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        Letter returnedLetter = emailDao.getLetter(TestConstants.TEST_ELTR_ID);
        assertNotNull(returnedLetter);
    }

    /**
     * Tests that the getLetter(int) method properly handles exceptions thrown from
     * the DB while querying the ltr table
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void getLetterByIdGetEltrExceptionTest() throws RollbackException {
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(LetterResultsSetExtractor.class))).thenThrow(new DataAccessResourceFailureException("Some DB exception occurred"));
        emailDao.getLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that the getLetter(int) method properly handles exceptions thrown from
     * the DB while getting the search parameters
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void getLetterByIdGetSearchParametersExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(LetterResultsSetExtractor.class))).thenReturn(mockedLetter).thenThrow(new DataAccessResourceFailureException("Some DB exception occurred"));
        emailDao.getLetter(TestConstants.TEST_ELTR_ID);
    }

    @Test
    public void findLetterHappyPathTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        SearchParameter searchParameter = new SearchParameter("Award_Id", "1234");
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(Collections.singletonList(TestConstants.TEST_ELTR_ID)).thenReturn(mockedLetter).thenReturn(mockedLetter.getSearchParameters());
        Set<String> validSearchParameters = new HashSet<String>();
        validSearchParameters.add(searchParameter.getKey());

        emailDao.findLetter(searchParameter, validSearchParameters);
    }

    @Test(expected=InvalidSearchParameterException.class)
    public void findLetterInvalidSearchParameterTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        SearchParameter searchParameter = new SearchParameter("Award_Id", "1234");

        emailDao.findLetter(searchParameter, Collections.emptySet());
    }

    @Test(expected = RollbackException.class)
    public void findLetterGetLetterIDsExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        SearchParameter searchParameter = new SearchParameter("Award_Id", "1234");
        Set<String> validSearchParameters = new HashSet<String>();
        validSearchParameters.add(searchParameter.getKey());
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenThrow(new DataAccessResourceFailureException("Exception during ID lookups"));
        emailDao.findLetter(searchParameter, validSearchParameters);
    }

    @Test(expected = RollbackException.class)
    public void findLetterGetLetterExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        SearchParameter searchParameter = new SearchParameter("Award_Id", "1234");
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(Collections.singletonList(TestConstants.TEST_ELTR_ID)).thenThrow(new DataAccessResourceFailureException("Exception during getLetter"));
        Set<String> validSearchParameters = new HashSet<String>();
        validSearchParameters.add(searchParameter.getKey());

        emailDao.findLetter(searchParameter, validSearchParameters);
    }

    /**
     * Tests that the normal behavior of the saveLetter method properly calls
     * the NamedParameterJdbcTemplate.update method to insert the letter in the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "insert"
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void saveLetterHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1);
        when(jdbcTemplateMock.query(any(String.class),any(Map.class), any(LetterResultsSetExtractor.class))).thenReturn(mockedLetter);
        emailDao.saveLetter(mockedLetter,null);
    }


    /**
     * Tests that the saveLetter method properly handles exceptions thrown from
     * the DB
     *
     * Mocks DB to throw an exception during the update query
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void saveLetterExceptionOnInsertEltrTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenThrow(new DataAccessResourceFailureException("Some DB exception occurred"));
        emailDao.saveLetter(mockedLetter,null);
    }

    /**
     * Tests that the saveLetter method properly handles exceptions thrown from
     * the DB
     *
     * Mocks DB to throw an exception when trying to get the inserted Letter from the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected=RollbackException.class)
    public void saveLetterExceptionOnGetAfterInsertTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1);
        when(jdbcTemplateMock.query(any(String.class),any(Map.class), any(LetterResultsSetExtractor.class))).thenThrow(new DataAccessResourceFailureException("Something failed on query"));

        emailDao.saveLetter(mockedLetter,null);
    }



    /**
     * Tests that the saveLetter method properly handles the case when the get query after insert returns no letter (null)
     *
     * Mocks DB to return null
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected=RollbackException.class)
    public void saveLetterNullOnGetAfterInsertTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1);
        when(jdbcTemplateMock.query(any(String.class),any(Map.class), any(LetterResultsSetExtractor.class))).thenReturn(null);

        emailDao.saveLetter(mockedLetter,null);
    }


    /**
     * Tests that the normal behavior of the updateLetter method properly calls
     * the NamedParameterJdbcTemplate.update method to update the letter in the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "update"
     * the DB
     *
     * In this scenario, the stored Letter has a different LetterStatus than the passed Letter
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void updateLetterHappyPathWithNewStatusTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        Letter passedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        mockedLetter.setEltrStatus(LetterStatus.Draft);
        passedLetter.setEltrStatus(LetterStatus.Sent);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza"))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenReturn(1);
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);

        Letter returnedLetter = emailDao.updateLetter(passedLetter);
        assertNotNull(returnedLetter);
    }

    /**
     * Tests that the normal behavior of the updateLetter method properly calls
     * the NamedParameterJdbcTemplate.update method to update the letter in the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "update"
     * the DB
     *
     * In this scenario, the stored Letter has the same LetterStatus as the passed Letter
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void updateLetterHappyPathWithSameStatusTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        Letter passedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        mockedLetter.setEltrStatus(LetterStatus.Draft);
        passedLetter.setEltrStatus(LetterStatus.Draft);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza"))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1);
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);

        Letter returnedLetter = emailDao.updateLetter(passedLetter);
        assertNotNull(returnedLetter);
    }


    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to return no Letter on the initial getLetter(int) call
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterLetterDoesNotExistTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(LetterResultsSetExtractor.class))).thenReturn(null);
        emailDao.updateLetter(mockedLetter);
    }
    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception during the initial getLetter(int) call
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterExceptionOnGetStoredLetterTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(LetterResultsSetExtractor.class))).thenThrow(new DataAccessResourceFailureException("Some DB exception occured on getLetter"));
        emailDao.updateLetter(mockedLetter);
    }

    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to return a Letter with LetterStatus.Sent set
     *
     * Test requires that a RollbackException is caught to pass because cannot update an already
     * sent Letter
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterLetterAlreadySentTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        mockedLetter.setEltrStatus(LetterStatus.Sent);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        emailDao.updateLetter(mockedLetter);
    }

    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception during an update query
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterExceptionDuringUpdateTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenThrow(new DataAccessResourceFailureException("Some exception occured"));
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);
        emailDao.updateLetter(mockedLetter);
    }

    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception when trying to insert into the eltr_mail_rcpt table
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterExceptionOnDeleteMailRcptTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenThrow(new DataAccessResourceFailureException("Exception occured during delete mail recipeints"));
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);

        emailDao.updateLetter(mockedLetter);
    }

    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception when trying to insert into the eltr_mail_rcpt table
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterExceptionOnMailRcptInsertTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenReturn(1).thenThrow(new DataAccessResourceFailureException("Exception occured during insert mail recipeint"));
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);

        emailDao.updateLetter(mockedLetter);
    }

    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DB
     *
     * Stubs the NamedParameterJdbcTemplate to throw an Exception when trying to retrieve the updated Letter
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void updateLetterExceptionOnGetAfterUpdateTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza"))).thenThrow(new DataAccessResourceFailureException("Some exception"));
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenReturn(1);
        emailDao.updateLetter(mockedLetter);
    }

    /**
     * Tests that the normal behavior of the storeSearchParameters method properly calls
     * the NamedParameterJdbcTemplate.update method to insert the parameters in the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "insert"
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void storeSearchParametersHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1);

        Set<String> parameterNames = new HashSet<String>();
        for( SearchParameter param : mockedLetter.getSearchParameters()){
            parameterNames.add(param.getKey());
        }
        emailDao.storeSearchParameters(mockedLetter, parameterNames);
    }

    /**
     * Tests that the storeSearchParameters method properly handles exceptions thrown due to search parameter
     * not found in lookup table
     **
     * Test requires that a InvalidSearchParameterException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = InvalidSearchParameterException.class)
    public void storeSearchParametersParameterNotFoundTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(0);
        emailDao.storeSearchParameters(mockedLetter, Collections.emptySet());
    }
    /**
     * Tests that the storeSearchParameters method properly handles exceptions thrown from
     * the DB
     *
     * Mocks DB to throw an exception during the update query
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void storeSearchParametersExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.queryForObject(any(String.class), any(Map.class), eq(Integer.class))).thenReturn(1);
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenThrow(new DataAccessResourceFailureException("Some DB exception occurred"));
        emailDao.storeSearchParameters(mockedLetter, Collections.emptySet());
    }

    /**
     * Tests that the normal behavior of the getSearchParameters method properly calls
     * the NamedParameterJdbcTemplate.query method to get the search parameters from the DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "query"
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void getSearchParametersHappyPathTest() throws RollbackException {
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        List<SearchParameter> parameters = emailDao.getSearchParameters(TestConstants.TEST_ELTR_ID);
        assertNotNull(parameters);
        assertTrue(parameters.size() > 0);
    }




    /**
     * Tests that the getSearchParameters method properly handles exceptions thrown from
     * the DB
     *
     * Mocks DB to throw an exception during the query
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void getSearchParametersExceptionTest() throws RollbackException {
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenThrow(new DataAccessResourceFailureException("Some DB exception occurred"));
        emailDao.getSearchParameters(TestConstants.TEST_ELTR_ID);
    }


    /**
     * Tests that the normal behavior of the deleteSearchParameters method properly calls
     * the NamedParameterJdbcTemplate.update method to delete the search parameters from the DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "update"
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void deleteSearchParametersHappyPathTest() throws RollbackException {
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1);
    }




    /**
     * Tests that the deleteSearchParameters method properly handles exceptions thrown from
     * the DB
     *
     * Mocks DB to throw an exception during the update
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void deleteSearchParametersExceptionTest() throws RollbackException {
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenThrow(new DataAccessResourceFailureException("Some DB exception occurred"));
        emailDao.deleteSearchParameters(TestConstants.TEST_ELTR_ID);
    }


    /**
     * Tests that the normal behavior of the updateLetter method properly calls
     * the NamedParameterJdbcTemplate.update method to delete the letter from the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "update"
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test
    public void deleteLetterHappyPath() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenReturn(1).thenReturn(1);

        Letter returnedLetter = emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
        assertNotNull(returnedLetter);
    }

    /**
     * Tests that the normal behavior of the updateLetter method properly calls
     * the NamedParameterJdbcTemplate.update method to delete the letter from the
     * DB
     *
     * Mocks the jdbcTemplate holding the DB connection to successfully "update"
     * the DB
     *
     * Asserts a non-null Letter object is returned
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class )
    public void deleteLetterSentStatusTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        mockedLetter.setEltrStatus(LetterStatus.Sent);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));

        emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that the deleteLetter method properly handles exceptions thrown because eltr does not exist
     *
     * Mocks the jdbcTemplate holding the DB connection to return no results when trying to lookup the eltr
     *
     * Asserts a ResourceNotFoundException is thrown
     *
     * @throws ResourceNotFoundException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = ResourceNotFoundException.class)
    public void deleteLetterLetterDoesntExistTest() throws RollbackException {
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(null);
        emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that the deleteLetter method properly handles exceptions thrown from the DB
     *
     * Mocks the jdbcTemplate holding the DB connection to throw an exception during the getLetter (existance check)
     * call
     *
     * Asserts a RollbackException is thrown
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void deleteLetterExceptionDuringGetTest() throws RollbackException {
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenThrow(new DataAccessResourceFailureException("Exception occured during getLetter"));
        emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that the deleteLetter method properly handles exceptions thrown from the DB
     *
     * Mocks the jdbcTemplate holding the DB connection to throw an exception during the delete
     * search parameters call
     *
     * Asserts a RollbackException is thrown
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void deleteLetterExceptionDuringDeleteSearchParametersTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenThrow(new DataAccessResourceFailureException("Exception occured during delete search parameters"));
        emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that the deleteLetter method properly handles exceptions thrown from the DB
     *
     * Mocks the jdbcTemplate holding the DB connection to throw an exception during the delete
     * mail recipients call
     *
     * Asserts a RollbackException is thrown
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void deleteLetterExceptionDuringDeleteMailRcptsTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenThrow(new DataAccessResourceFailureException("Exception occured during delete mail recipients"));
        emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
    }


    /**
     * Tests that the deleteLetter method properly handles exceptions thrown from the DB
     *
     * Mocks the jdbcTemplate holding the DB connection to throw an exception during the delete
     * eltr call
     *
     * Asserts a RollbackException is thrown
     *
     * @throws RollbackException
     */
    @SuppressWarnings("unchecked")
    @Test(expected = RollbackException.class)
    public void deleteLetterExceptionDuringDeleteEltrTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(jdbcTemplateMock.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(mockedLetter).thenReturn(Collections.singletonList(new SearchParameter("1", "pizza")));
        when(jdbcTemplateMock.update(any(String.class), any(Map.class))).thenReturn(1).thenReturn(1).thenThrow(new DataAccessResourceFailureException("Exception occured during delete eltr"));
        emailDao.deleteLetter(TestConstants.TEST_ELTR_ID);
    }
}
