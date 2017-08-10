package gov.nsf.emailservice.validator;


import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseError;
import gov.nsf.common.model.BaseResponseWrapper;
import gov.nsf.emailservice.api.model.EmailInfo;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterStatus;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestUtils;
import gov.nsf.referencedataservice.api.model.ApplicationInfo;
import gov.nsf.referencedataservice.api.service.ApplicationInfoService;
import gov.nsf.templateservice.model.LetterTemplate;
import gov.nsf.templateservice.model.LetterTemplateResponseWrapper;
import gov.nsf.templateservice.service.TemplateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static gov.nsf.emailservice.common.util.TestUtils.assertContains;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * JUnit tests for CreateLetterValidator
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateLetterValidatorTest {


    @InjectMocks
    public CreateLetterValidator validator;

    @Mock
    private TemplateService templateServiceMock;

    @Mock
    private ApplicationInfoService applicationInfoServiceMock;


    @Before
    public void setup() throws Exception {
        when(templateServiceMock.getLetterTemplate(anyString())).thenReturn(new LetterTemplateResponseWrapper(new LetterTemplate()));
        when(applicationInfoServiceMock.getApplicationInfo(anyString())).thenReturn(new ApplicationInfo());

        assertNotNull(validator.getApplicationInfoServiceClient());
        assertNotNull(validator.getTemplateServiceClient());
    }

    @Test
    public void validateRequestHappyPathTest() throws FormValidationException, RollbackException {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        validator.validateRequest(letter);
    }

    @Test
    public void validateRequestNullLetterTest() throws FormValidationException {
        Letter letter = null;

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.LETTER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.LETTER_FIELD), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestNullEmailInfoTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setApplID("01");
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setEmailInfo(null);
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.EMAIL_INFO_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.EMAIL_INFO_FIELD), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestAllNullValuesTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent(null);
        letter.setEltrStatus(null);
        letter.setEltrStatusUser(null);
        letter.setApplID(null);
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject(null);
        letter.getEmailInfo().setFromAddress(null);
        letter.getEmailInfo().setToAddresses(null);
        letter.getEmailInfo().setCcAddresses(null);
        letter.getEmailInfo().setBccAddresses(null);
        letter.setSearchParameters(null);


        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            System.out.println(ex.getValidationErrors());
            assertEquals(10, ex.getValidationErrors().size());
        }
    }

    @Test
    public void validateRequestAllNullValuesWithDraftStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent(null);
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser(null);
        letter.setPlainText(false);
        letter.setApplID(null);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject(null);
        letter.getEmailInfo().setFromAddress(null);
        letter.getEmailInfo().setToAddresses(null);
        letter.getEmailInfo().setCcAddresses(null);
        letter.getEmailInfo().setBccAddresses(null);
        letter.setSearchParameters(null);

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(9, ex.getValidationErrors().size());
        }
    }

    @Test
    public void validateRequestAllNullValuesWithSentStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent(null);
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser(null);
        letter.setPlainText(false);
        letter.setApplID(null);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject(null);
        letter.getEmailInfo().setFromAddress(null);
        letter.getEmailInfo().setToAddresses(null);
        letter.getEmailInfo().setCcAddresses(null);
        letter.getEmailInfo().setBccAddresses(null);
        letter.setSearchParameters(null);


        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(9, ex.getValidationErrors().size());
        }
    }

    @Test
    public void validateRequestEmptyStringsWithDraftStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("");
        letter.getEmailInfo().setFromAddress("");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            System.out.println(ex.getValidationErrors());
            assertEquals(2, ex.getValidationErrors().size());
            assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_USER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_USER_FIELD),
                    new BaseError(Constants.SENDER_ADDRESS_FIELD, Constants.INVALID_ADDRESS_FORMAT + Constants.SENDER_ADDRESS_FIELD));
        }
    }

    @Test
    public void validateRequestEmptyStringsWithSentStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("");
        letter.getEmailInfo().setFromAddress("");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(4, ex.getValidationErrors().size());
        }
    }

    @Test
    public void validateRequestEmptyToRecipientsWithSentStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.<String>emptyList());
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertTrue(ex.getValidationErrors().size() == 1);
            assertEquals(new BaseError(Constants.TO_RECIPIENTS_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.TO_RECIPIENTS_FIELD), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestWhiteSpaceAndInvalidToAddressesTest() throws FormValidationException {
        List<String> invalidToAddresses = new ArrayList<String>();
        invalidToAddresses.add("");
        invalidToAddresses.add("©");
        invalidToAddresses.add("test@nsf.gov");

        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(invalidToAddresses);
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.TO_RECIPIENTS_FIELD, Constants.INVALID_ADDRESS_FORMAT + Constants.TO_RECIPIENTS_FIELD), ex.getValidationErrors().get(0));
        }

    }

    @Test
    public void validateRequestInvalidAddressesInToRecipientsTest() throws FormValidationException {
        List<String> invalidToAddresses = new ArrayList<String>();
        invalidToAddresses.add("©");
        invalidToAddresses.add("©");
        invalidToAddresses.add("©");

        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setApplID("MyNSF");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(invalidToAddresses);
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.TO_RECIPIENTS_FIELD, Constants.INVALID_ADDRESS_FORMAT + Constants.TO_RECIPIENTS_FIELD), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestIllegalCharactersTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("©");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setApplID("01");
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("©");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(2, ex.getValidationErrors().size());
        }
    }

    @Test
    public void validateRequestEmptySearchParametersTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.<SearchParameter>emptyList());

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.SEARCH_PARAMETERS_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.SEARCH_PARAMETERS_FIELD), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestNullKeySearchParamterTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter(null, "some value")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.SEARCH_PARAMETERS_FIELD, Constants.INVALID_PARAMETER_KEY_OR_VALUE), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestNullValueSearchParamterTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setApplID("01");
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("some key", null)));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.SEARCH_PARAMETERS_FIELD, Constants.INVALID_PARAMETER_KEY_OR_VALUE), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestTemplateDoesntExistTest() throws Exception {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setTmplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        LetterTemplateResponseWrapper wrapper = new LetterTemplateResponseWrapper();
        List<BaseError> errors = Collections.singletonList(new BaseError("Some field", "Some desc"));
        wrapper.setErrors(errors);

        when(templateServiceMock.getLetterTemplate(anyString())).thenReturn(wrapper);

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(ex.getValidationErrors().get(0), errors.get(0));
        }
    }

    @Test
    public void validateRequestTemplateCallExceptionTest() throws Exception {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("01");
        letter.setTmplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        LetterTemplateResponseWrapper wrapper = new LetterTemplateResponseWrapper();
        List<BaseError> errors = Collections.singletonList(new BaseError("Some field", "Some desc"));
        wrapper.setErrors(errors);

        when(templateServiceMock.getLetterTemplate(anyString())).thenThrow(new RollbackException());

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
        }
    }

    @Test
    public void validateRequestApplIDDoesntExistTest() throws Exception {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("900");
        letter.setTmplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        LetterTemplateResponseWrapper wrapper = new LetterTemplateResponseWrapper();
        List<BaseError> errors = Collections.singletonList(new BaseError("Some field", "Some desc"));
        wrapper.setErrors(errors);

        when(applicationInfoServiceMock.getApplicationInfo(anyString())).thenReturn(null);

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(ex.getValidationErrors().get(0), new BaseError(Constants.APPL_ID_FIELD, Constants.APPL_ID_DOES_NOT_EXIST + letter.getApplID()));

        }
    }

    @Test
    public void validateRequestApplIDNonIntegerTest() throws Exception {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("fish");
        letter.setTmplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        LetterTemplateResponseWrapper wrapper = new LetterTemplateResponseWrapper();
        List<BaseError> errors = Collections.singletonList(new BaseError("Some field", "Some desc"));
        wrapper.setErrors(errors);

        when(applicationInfoServiceMock.getApplicationInfo(anyString())).thenReturn(null);

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(ex.getValidationErrors().get(0), new BaseError(Constants.APPL_ID_FIELD, Constants.APPL_ID_NON_NUMERIC));

        }
    }

    @Test
    public void validateRequestInvalidStatusTest() throws Exception {
        Letter letter = new Letter();
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Invalid);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("42");
        letter.setTmplID("01");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        LetterTemplateResponseWrapper wrapper = new LetterTemplateResponseWrapper();
        List<BaseError> errors = Collections.singletonList(new BaseError("Some field", "Some desc"));
        wrapper.setErrors(errors);

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            assertEquals(ex.getValidationErrors().get(0), new BaseError(Constants.ELTR_STATUS_FIELD, Constants.INVALID_ELTR_STATUS));

        }
    }

}
