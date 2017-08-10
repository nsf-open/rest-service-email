package gov.nsf.emailservice.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendLevelEnum;
import gov.mynsf.common.email.model.SendMetaData;
import gov.mynsf.common.email.util.NsfEmailUtil;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.*;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;
import gov.nsf.emailservice.dao.EmailDaoImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * EmailService JUnit tests
 *
 */

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private EmailDaoImpl emailDao;
    
    @Mock
    private NsfEmailUtil nsfEmailUtil;


    @Before
    public void checkFieldsNotNull() {
        assertNotNull(emailService);
        assertNotNull(emailService.getEmailDao());
    }

    /**
     * Tests that the normal behavior of the getLetter method properly calls
     * the EmailDao.getLetter method and returns a LetterResponseWrapper object from the DAO
     *
     * Mocks the EmailDao to return the Letter object
     *
     * Asserts that a non-null LetterResponseWrapper object is returned
     *
     * @throws RollbackException
     */
    @Test
    public void getLetterHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.getLetter(TestConstants.TEST_ELTR_ID)).thenReturn(mockedLetter);
        LetterResponseWrapper returnedLetterWrapper = emailService.getLetter(TestConstants.TEST_ELTR_ID);
        assertNotNull(returnedLetterWrapper);
    }

    /**
     * Tests that the getLetter method properly handles exceptions thrown from
     * the DAO
     *
     * Stubs the EmailDao to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @Test(expected = RollbackException.class)
    public void getLetterExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.getLetter(TestConstants.TEST_ELTR_ID)).thenThrow(new RollbackException(Constants.ERROR_GETTING_LETTER));
        emailService.getLetter(TestConstants.TEST_ELTR_ID);
    }

    @Test
    public void findLetterHappyPathTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        SearchParameter searchParameter = new SearchParameter("Award_Id", "1234");
        when(emailDao.findLetter(any(SearchParameter.class), any(Set.class))).thenReturn(Collections.singletonList(mockedLetter));
        when(emailDao.getSearchParameterNames()).thenReturn(new HashSet<String>());
        LetterListResponseWrapper wrapper = emailService.findLetter(searchParameter);
        assertNotNull(wrapper);
        assertNotNull(wrapper.getLetters());

    }

    @Test( expected = RollbackException.class )
    public void findLetterExceptionTest() throws Exception {
        SearchParameter searchParameter = new SearchParameter("Award_Id", "1234");
        when(emailDao.getSearchParameterNames()).thenReturn(new HashSet<String>());
        when(emailDao.findLetter(any(SearchParameter.class), any(Set.class))).thenThrow(new RollbackException("Some exception occured at the DAO"));
        emailService.findLetter(searchParameter);


    }

    /**
     * Tests that the normal behavior of the saveLetter method properly calls
     * the EmailDao.saveLetter method and returns a LetterResponseWrapper
     *
     * Mocks the EmailDao to return the Letter object
     *
     * Asserts that a non-null LetterResponseWrapper object is returned
     *
     * @throws RollbackException
     */
    @Test
    public void saveLetterHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.saveLetter(mockedLetter,null)).thenReturn(mockedLetter);
        when(emailDao.getSearchParameterNames()).thenReturn(new HashSet<String>());
        doNothing().when(emailDao).storeSearchParameters(any(Letter.class), any(Set.class));
        LetterResponseWrapper returnedLetterWrapper = emailService.saveLetter(mockedLetter);
        assertNotNull(returnedLetterWrapper);
    }

    /**
     * Tests that the saveLetter method properly handles exceptions thrown from
     * the DAO
     *
     * Stubs the EmailDao to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @Test(expected = RollbackException.class)
    public void saveLetterExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.saveLetter(mockedLetter,null)).thenThrow(new RollbackException(Constants.ERROR_SAVING_LETTER));
        emailService.saveLetter(mockedLetter);
    }


    /**
     * Tests that the normal behavior of the updateLetter method properly calls
     * the EmailDao.updateLetter method and returns a Letter object from the DAO
     *
     * Mocks the EmailDao to return the Letter object
     *
     * Asserts that a non-null LetterResponseWrapper object is returned
     *
     * @throws RollbackException
     */
    @Test
    public void updateLetterHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.updateLetter(mockedLetter)).thenReturn(mockedLetter);
        LetterResponseWrapper returnedLetterWrapper = emailService.updateLetter(mockedLetter);
        assertNotNull(returnedLetterWrapper);
    }

    /**
     * Tests that the normal behavior of the saveLetter method properly calls
     * the EmailDao.saveLetter method and returns a LetterResponseWrapper
     *
     * Mocks the EmailDao to return the Letter object
     *
     * Asserts that a non-null LetterResponseWrapper object is returned
     *
     * @throws RollbackException
     */
    @Test
    public void saveLetterWithTemplateHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.saveLetter(mockedLetter,null)).thenReturn(mockedLetter);
        LetterResponseWrapper returnedLetterWrapper = emailService.saveLetter(mockedLetter,null);
        assertNotNull(returnedLetterWrapper);
    }

    /**
     * Tests that the saveLetter method properly handles exceptions thrown from
     * the DAO
     *
     * Stubs the EmailDao to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @Test(expected = RollbackException.class)
    public void saveLetterWithTemplateExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.saveLetter(mockedLetter,null)).thenThrow(new RollbackException(Constants.ERROR_SAVING_LETTER));
        emailService.saveLetter(mockedLetter,null);
    }

    /**
     * Tests that the updateLetter method properly handles exceptions thrown from
     * the DAO
     *
     * Stubs the EmailDao to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @Test(expected = RollbackException.class)
    public void updateLetterExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.updateLetter(mockedLetter)).thenThrow(new RollbackException(Constants.ERROR_UPDATING_LETTER));
        emailService.updateLetter(mockedLetter);
    }

    @Test
    public void sendLetterDebugModeHappyPathTest() {
        EmailRequest emailRequest = TestUtils.getMockEmailRequest();
        Letter letter = TestUtils.convertEmailRequestToSendLetterRequest(emailRequest).getLetter();
        SendMetaData metaData = emailRequest.getSendMetaData();
        doNothing().when(nsfEmailUtil).sendEmail(emailRequest);
        when(nsfEmailUtil.getSendLevel()).thenReturn(SendLevelEnum.DebugLevel);
        emailService.sendLetter(letter,metaData);
    }

    @Test
    public void sendLetterNonDebugModeHappyPathTest() {
        EmailRequest emailRequest = TestUtils.getMockEmailRequest();
        Letter letter = TestUtils.convertEmailRequestToSendLetterRequest(emailRequest).getLetter();
        SendMetaData metaData = emailRequest.getSendMetaData();
        doNothing().when(nsfEmailUtil).sendEmail(emailRequest);
        when(nsfEmailUtil.getSendLevel()).thenReturn(SendLevelEnum.ProdLevel);
        emailService.sendLetter(letter,metaData);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sendLetterNullSendMetaDataTest() {
        EmailRequest emailRequest = TestUtils.getMockEmailRequest();
        Letter letter = TestUtils.convertEmailRequestToSendLetterRequest(emailRequest).getLetter();
        SendMetaData metaData = null;
        when(nsfEmailUtil.getSendLevel()).thenReturn(SendLevelEnum.DebugLevel);
        emailService.sendLetter(letter, metaData);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sendLetterInvalidDebugRecipientTest() {
        EmailRequest emailRequest = TestUtils.getMockEmailRequest();
        Letter letter = TestUtils.convertEmailRequestToSendLetterRequest(emailRequest).getLetter();
        SendMetaData metaData = emailRequest.getSendMetaData();
        metaData.setDebugRecipients(Collections.singletonList(""));
        when(nsfEmailUtil.getSendLevel()).thenReturn(SendLevelEnum.DebugLevel);
        emailService.sendLetter(letter, metaData);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sendLetterNullDebugRecipientsTest() {
        EmailRequest emailRequest = TestUtils.getMockEmailRequest();
        Letter letter = TestUtils.convertEmailRequestToSendLetterRequest(emailRequest).getLetter();
        SendMetaData metaData = emailRequest.getSendMetaData();
        metaData.setDebugRecipients(null);
        when(nsfEmailUtil.getSendLevel()).thenReturn(SendLevelEnum.DebugLevel);
        emailService.sendLetter(letter, metaData);
    }

    @Test(expected=IllegalArgumentException.class)
    public void sendLetterMissingDebugRecipientsTest() {
        EmailRequest emailRequest = TestUtils.getMockEmailRequest();
        Letter letter = TestUtils.convertEmailRequestToSendLetterRequest(emailRequest).getLetter();
        SendMetaData metaData = emailRequest.getSendMetaData();
        metaData.setDebugRecipients(Collections.<String>emptyList());
        when(nsfEmailUtil.getSendLevel()).thenReturn(SendLevelEnum.DebugLevel);
        emailService.sendLetter(letter, metaData);
    }

    /**
     * Tests that the normal behavior of the saveLetter method properly calls
     * the EmailDao.deleteLetter method and returns a LetterResponseWrapper
     *
     * Mocks the EmailDao to return the Letter object
     *
     * Asserts that a non-null LetterResponseWrapper object is returned
     *
     * @throws RollbackException
     */
    @Test
    public void deleteLetterHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.deleteLetter(TestConstants.TEST_ELTR_ID)).thenReturn(mockedLetter);
        LetterResponseWrapper returnedLetterWrapper = emailService.deleteLetter(TestConstants.TEST_ELTR_ID);
        assertNotNull(returnedLetterWrapper);
    }

    /**
     * Tests that the deleteLetter method properly handles exceptions thrown from
     * the DAO
     *
     * Stubs the EmailDao to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @Test(expected = RollbackException.class)
    public void deleteLetterWithTemplateExceptionTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.deleteLetter(TestConstants.TEST_ELTR_ID)).thenThrow(new RollbackException(Constants.ERROR_DELETING_LETTER));
        emailService.deleteLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that the normal behavior of the getLetter method properly calls
     * the EmailDao.getLetter method and returns a Letter object from the DAO
     *
     * Mocks the EmailDao to return the Letter object
     *
     * Asserts that a non-null SearchParameterResponseWrapper object is returned
     *
     * @throws RollbackException
     */
    @Test
    public void getSearchParametersHappyPathTest() throws RollbackException {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        when(emailDao.getLetter(TestConstants.TEST_ELTR_ID)).thenReturn(mockedLetter);
        SearchParameterResponseWrapper returnedWrapper = emailService.getSearchParameters(TestConstants.TEST_ELTR_ID);
        assertNotNull(returnedWrapper);
    }

    /**
     * Tests that the getSearchParameters method properly handles exceptions thrown from
     * the DAO
     *
     * Stubs the EmailDao to throw an Exception
     *
     * Test requires that a RollbackException is caught to pass
     *
     * @throws RollbackException
     */
    @Test(expected = RollbackException.class)
    public void getSearchParametersExceptionTest() throws RollbackException {
        when(emailDao.getLetter(TestConstants.TEST_ELTR_ID)).thenThrow(new RollbackException(Constants.ERROR_GETTING_LETTER));
        emailService.getSearchParameters(TestConstants.TEST_ELTR_ID);
    }
 }


