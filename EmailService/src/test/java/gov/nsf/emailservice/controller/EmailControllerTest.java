package gov.nsf.emailservice.controller;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.*;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;

import gov.nsf.emailservice.api.service.EmailService;
import gov.nsf.emailservice.validator.CreateLetterValidator;
import gov.nsf.emailservice.validator.EmailServiceValidatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

/**
 * EmailController JUnit tests
 *
 */
@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class EmailControllerTest {

    @InjectMocks
    private EmailController controller;

    @Spy
    private EmailServiceValidatorFactory factory;

    @Mock
    private CreateLetterValidator createLetterValidator;

    @Mock
    private EmailService serviceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    /**
     * Tests that a GET request to the saveLetter route returns a 200 OK response
     * <p>
     * Mocks the EmailService.getLetter to return a Letter object
     */
    @Test
    public void getLetterRequestHappyPathTest() throws Exception {

        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID;
        when(serviceMock.getLetter(TestConstants.TEST_ELTR_ID)).thenReturn(new LetterResponseWrapper(mockedLetter));
        mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).getLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that a GET request to the getLetter route returns a 500 response
     * when a Service error is encountered
     * <p>
     * Mocks the EmailService.getLetter to throw a RollbackException
     */
    @Test
    public void getLetterRequestServerErrorTest() throws Exception {
        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID;
        when(serviceMock.getLetter(TestConstants.TEST_ELTR_ID)).thenThrow(new RollbackException("Some exception occurred"));
        mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).getLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that a GET request to the saveLetter route returns a 200 OK response
     * <p>
     * Mocks the EmailService.getLetter to return a Letter object
     */
    @Test
    public void getSearchParametersRequestHappyPathTest() throws Exception {

        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID + "/searchparameters";
        when(serviceMock.getSearchParameters(TestConstants.TEST_ELTR_ID)).thenReturn(new SearchParameterResponseWrapper(mockedLetter.getSearchParameters()));
        mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).getSearchParameters(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that a GET request to the getLetter route returns a 500 response
     * when a Service error is encountered
     * <p>
     * Mocks the EmailService.getLetter to throw a RollbackException
     */
    @Test
    public void getSearchParametersRequestServerErrorTest() throws Exception {
        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID + "/searchparameters";
        when(serviceMock.getSearchParameters(TestConstants.TEST_ELTR_ID)).thenThrow(new RollbackException("Some exception occurred"));
        mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).getSearchParameters(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that a POST request to the saveLetter route returns a 200 OK response
     * <p>
     * Mocks the EmailService.saveLetter to return a Letter object
     */
    @Test
    public void saveLetterRequestHappyPathTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenReturn(new LetterResponseWrapper(mockedLetter));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }


    /**
     * Tests that a POST request to the saveLetter route returns a 500 response
     * when a Service error is encountered
     * <p>
     * Mocks the EmailService.saveLetter to throw a RollbackException
     */
    @Test
    public void saveLetterRequestServerErrorTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new RollbackException("Some exception occurred"));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }

    /**
     * Tests that a PUT request to the saveLetter route returns a 200 OK response
     * <p>
     * Mocks the EmailService.updateLetter to return a Letter object
     */
    @Test
    public void updateLetterRequestHappyPathTest() throws Exception {

        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);

        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID;
        when(serviceMock.updateLetter(any(Letter.class))).thenReturn(new LetterResponseWrapper(mockedLetter));
        mockMvc.perform(put(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).updateLetter(any(Letter.class));
    }

    /**
     * Tests that a PUT request to the saveLetter route returns a 500 response
     * when a Service error is encountered
     * <p>
     * Mocks the EmailService.updateLetter to throw a RollbackException
     */
    @Test
    public void updateLetterRequestServerErrorTest() throws Exception {

        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);

        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID;
        when(serviceMock.updateLetter(any(Letter.class))).thenThrow(new RollbackException("Some exception occurred"));
        mockMvc.perform(put(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().is5xxServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).updateLetter(any(Letter.class));
    }

    /**
     * Tests that a GET request to the findLetter route returns a 200 OK response
     * <p>
     * Mocks the EmailService.findLetter to return a Letter object
     */
    @Test
    public void findLetterRequestHappyPathTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        SearchParameter searchParameter = new SearchParameter("Award_Id", "10001");
        String URL = TestConstants.LETTER_ENDPOINT + "?Award_Id=10001";
        when(serviceMock.findLetter(any(SearchParameter.class))).thenReturn(new LetterListResponseWrapper(Collections.singletonList(mockedLetter)));
        mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).findLetter(eq(searchParameter));
    }

    /**
     * Tests that a GET request to the findLetter route returns a 500 response
     * when a Service error is encountered
     * <p>
     * Mocks the EmailService.findLetter to throw a RollbackException
     */
    @Test
    public void findLetterRequestServerErrorTest() throws Exception {
        SearchParameter searchParameter = new SearchParameter("Award_Id", "10001");
        String URL = TestConstants.LETTER_ENDPOINT + "?Award_Id=10001";
        when(serviceMock.findLetter(any(SearchParameter.class))).thenThrow(new RollbackException("Some exception occured"));
        mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).findLetter(eq(searchParameter));
    }



    /**
     * Tests that a DELETE request to the findLetter route returns a 200 OK response
     * <p>
     * Mocks the EmailService.findLetter to return a Letter object
     */
    @Test
    public void deleteLetterRequestHappyPathTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID;
        when(serviceMock.deleteLetter(TestConstants.TEST_ELTR_ID)).thenReturn(new LetterResponseWrapper(mockedLetter));
        mockMvc.perform(delete(URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).deleteLetter(TestConstants.TEST_ELTR_ID);
    }

    /**
     * Tests that a DELETE request to the findLetter route returns a 500 response
     * when a Service error is encountered
     * <p>
     * Mocks the EmailService.findLetter to throw a RollbackException
     */
    @Test
    public void deleteLetterRequestServerErrorTest() throws Exception {
        String URL = TestConstants.LETTER_ENDPOINT + "/" + TestConstants.TEST_ELTR_ID;
        when(serviceMock.deleteLetter(TestConstants.TEST_ELTR_ID)).thenThrow(new RollbackException("Some service exception occured"));
        MvcResult result = mockMvc.perform(delete(URL).contentType(MediaType.APPLICATION_JSON)).andReturn();
        verify(serviceMock, times(1)).deleteLetter(TestConstants.TEST_ELTR_ID);
    }
}
