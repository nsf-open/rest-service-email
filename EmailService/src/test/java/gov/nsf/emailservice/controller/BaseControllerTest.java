package gov.nsf.emailservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.nsf.common.exception.ResourceNotFoundException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.emailservice.api.model.InvalidSearchParameterException;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.service.EmailService;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;
import gov.nsf.emailservice.validator.CreateLetterValidator;
import gov.nsf.emailservice.validator.EmailServiceValidatorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jacklinden on 1/30/17.
 */
@RunWith(MockitoJUnitRunner.class)
@WebAppConfiguration
public class BaseControllerTest {

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

    @Test
    public void invalidSearchParameterExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new InvalidSearchParameterException("Invalid search parameter passed"));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isBadRequest()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }

    @Test
    public void resourceNotFoundExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new ResourceNotFoundException("Resource not found"));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isNotFound()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }

    @Test
    public void rollbackExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new RollbackException("Resource not found"));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isInternalServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }

    @Test
    public void accessDeniedExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new AccessDeniedException("Access Denied"));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isInternalServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }


    @Test
    public void mailExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new MailException("Mail exception") {
        });
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isInternalServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }
    @Test
    public void generalExceptionTest() throws Exception {
        Letter mockedLetter = TestUtils.getMockLetter(TestConstants.TEST_ELTR_ID);
        String URL = TestConstants.LETTER_ENDPOINT + "?templateID=1";
        when(factory.getValidator("createLetter")).thenReturn(createLetterValidator);
        doNothing().when(createLetterValidator).validateRequest(any(Letter.class));
        when(serviceMock.saveLetter(any(Letter.class),any(String.class))).thenThrow(new RuntimeException("General exception"));
        mockMvc.perform(post(URL).content(new ObjectMapper().writeValueAsString(mockedLetter)).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isInternalServerError()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
        verify(serviceMock, times(1)).saveLetter(any(Letter.class),any(String.class));
    }


}
