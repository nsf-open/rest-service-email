package gov.nsf.emailservice.dao;

import gov.nsf.common.exception.ResourceNotFoundException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * EmailDao DB integration tests
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"Embedded-EmailDaoTest-Context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EmailDaoIntgTest {

    @Autowired
    private EmailDaoImpl emailDao;


    @Before
    public void checkFieldsNotNull() {
        assertNotNull(emailDao);
        assertNotNull(emailDao.getDataSource());
        assertNotNull(emailDao.getJdbcTemplate());
    }

    private static final String EXISTING_ELTR_ID = "1";
    private static final String NON_EXISTING_ELTR_ID = "400";
    private static final String ELTR_ID_WITH_SENT_STATUS = "137"; //Refers to a Letter in the DB with eltrStatus == Sent

    @Test
    public void getSearchParameterNamesCacheTest() throws Exception{
        NamedParameterJdbcTemplate mockTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        emailDao.setJdbcTemplate(mockTemplate);

        when(mockTemplate.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(new HashSet<String>());
        emailDao.getSearchParameterNames();
        emailDao.getSearchParameterNames();
        emailDao.getSearchParameterNames();

        verify(mockTemplate, times(1)).query(any(String.class), any(Map.class), any(ResultSetExtractor.class));
    }

    /**
     * Tests that the dao.getLetter successfully returns a Letter object from the DB when an existing ID is passed
     *
     * @throws Exception
     */
    @Test
    public void getLetterHappyPathTest() throws Exception {
        Letter letter = emailDao.getLetter(EXISTING_ELTR_ID);
        assertNotNull(letter);
        assertEquals(EXISTING_ELTR_ID, letter.getEltrID());
        System.out.println(letter);
    }

    /**
     * Tests that the dao.getLetter throws a ResourceNotFoundException when a non-existing ID is passed
     *
     * @throws Exception
     */
    @Test(expected=ResourceNotFoundException.class)
    //@Ignore
    public void getLetterNonExistentIDTest() throws Exception {
        emailDao.getLetter(NON_EXISTING_ELTR_ID);
    }

    /**
     * Tests that the dao.saveLetter successfully inserts the Letter into the the eltr_eltr and eltr_mail_rcpt tables
     *
     * Performs a getLetter on the ID of the returned savedLetter to assert this operation was successful
     *
     * @throws Exception
     */
    @Test
    public void saveLetterHappyPathTest() throws Exception {

        Letter returnedLetter = emailDao.saveLetter(TestUtils.getMockLetter(null),null);
        assertNotNull(returnedLetter);
        assertNotNull(returnedLetter.getEltrID());

        Letter savedLetter = emailDao.getLetter(returnedLetter.getEltrID());
        assertNotNull(savedLetter);
        assertNotNull(savedLetter.getEltrID());
        assertEquals(returnedLetter.getEltrID(), savedLetter.getEltrID());
        assertEquals(returnedLetter.getEltrContent(), savedLetter.getEltrContent());

        System.out.println(returnedLetter);
        System.out.println(savedLetter);
    }


    /**
     * Tests that the dao.updateLetter successfully updates the DB tables with new information
     *
     * Asserts that the information persists using the dao.getLetter on the same ID
     *
     * @throws Exception
     */
    @Test
    public void updateLetterHappyPathTest() throws Exception {

        Letter oldLetter = emailDao.getLetter(EXISTING_ELTR_ID);
        String oldContent = oldLetter.getEltrContent();

        Letter inputLetter = TestUtils.getMockLetter(EXISTING_ELTR_ID);
        inputLetter.setEltrContent("This is the new eltrContent");
        String newContent = inputLetter.getEltrContent();

        Letter returnedLetter = emailDao.updateLetter(inputLetter);
        assertNotNull(returnedLetter);
        assertNotNull(returnedLetter.getEltrContent());
        assertNotEquals(returnedLetter.getEltrContent(), oldContent);
        assertEquals(returnedLetter.getEltrContent(), newContent);

        Letter updatedLetter = emailDao.getLetter(EXISTING_ELTR_ID);
        assertNotNull(updatedLetter);
        assertNotNull(updatedLetter.getEltrContent());
        assertNotEquals(updatedLetter.getEltrContent(), oldContent);
        assertEquals(updatedLetter.getEltrContent(), newContent);
    }

    /**
     * Tests that the dao.updateLetter throws a ResourceNotFoundException when a non-existing ID is passed
     *
     * Test expects a ResourceNotFoundException for this scenario
     *
     * @throws Exception
     */
    @Test(expected=ResourceNotFoundException.class)
    public void updateLetterNonExistentIDTest() throws Exception {
        Letter inputLetter = TestUtils.getMockLetter(NON_EXISTING_ELTR_ID);
        emailDao.updateLetter(inputLetter);
    }

    /**
     * Tests that the dao.updateLetter throws a RollbackException when trying to update a Letter
     * in the DB that has already been sent (eltr_stts_code == S)
     *
     * Test expects a RollbackException for this scenario
     *
     * @throws Exception
     */
    @Test(expected=RollbackException.class)
    public void updateLetterLetterAlreadySentTest() throws Exception {
        Letter inputLetter = TestUtils.getMockLetter(ELTR_ID_WITH_SENT_STATUS);
        emailDao.updateLetter(inputLetter);
    }

    /**
     * Tests that the dao.getLetter successfully returns a Letter object from the DB when an existing ID is passed
     *
     * @throws Exception
     */
    @Test
    public void getSearchParametersTest() throws Exception {
        List<SearchParameter> searchParameters = emailDao.getSearchParameters(EXISTING_ELTR_ID);
        assertNotNull(searchParameters);
        assertTrue(!searchParameters.isEmpty());
        System.out.println(searchParameters);
    }


}
