package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.model.BaseError;
import gov.nsf.emailservice.api.model.EmailInfo;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterStatus;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * JUnit tests for UpdateLetterValidator
 */
public class UpdateLetterValidatorTest {


    public UpdateLetterValidator validator = new UpdateLetterValidator();

    @Test
    public void validateRequestHappyPathTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
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
            assertEquals(1,ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),new BaseError(Constants.LETTER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.LETTER_FIELD));
        }
    }

    @Test
    public void validateRequestNullEmailInfoTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setApplID("MyNSF");
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setEmailInfo(null);
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1,ex.getValidationErrors().size());
            assertEquals(new BaseError(Constants.EMAIL_INFO_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.EMAIL_INFO_FIELD), ex.getValidationErrors().get(0));
        }
    }

    @Test
    public void validateRequestAllNullValuesTest() throws FormValidationException {

        Letter letter = new Letter();
        letter.setEltrID(null);
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
        letter.setEltrID(null);
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
        letter.setEltrID(null);
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
        letter.setEltrID("");
        letter.setEltrContent("");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("");
        letter.setApplID("MyNSF");
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
            assertEquals(3, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_ID_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_ID_FIELD),
                    new BaseError(Constants.ELTR_USER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_USER_FIELD),
                    new BaseError(Constants.SENDER_ADDRESS_FIELD, Constants.INVALID_ADDRESS_FORMAT + Constants.SENDER_ADDRESS_FIELD));

        }
    }

    @Test
    public void validateRequestEmptyStringsWithSentStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID("");
        letter.setEltrContent("");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("");
        letter.setApplID("MyNSF");
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
            assertEquals(5, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_ID_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_ID_FIELD),
                    new BaseError(Constants.ELTR_USER_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_USER_FIELD),
                    new BaseError(Constants.SENDER_ADDRESS_FIELD, Constants.INVALID_ADDRESS_FORMAT + Constants.SENDER_ADDRESS_FIELD),
                    new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.MAIL_SUBJECT_FIELD),
                    new BaseError(Constants.ELTR_CONTENT_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_CONTENT_FIELD)
                    );

        }
    }

    @Test
    public void validateRequestInvalidNumberStringID() throws FormValidationException{
        Letter letter = new Letter();
        letter.setEltrID("fish");
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
        letter.setPlainText(false);
        letter.setEmailInfo(new EmailInfo());
        letter.getEmailInfo().setMailSubject("Test Subject");
        letter.getEmailInfo().setFromAddress("test@nsf.gov");
        letter.getEmailInfo().setToAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setCcAddresses(Collections.singletonList("test@nsf.gov"));
        letter.getEmailInfo().setBccAddresses(Collections.singletonList("test@nsf.gov"));
        letter.setSearchParameters(Collections.singletonList(new SearchParameter("who's on first", "idk")));

        try {
            validator.validateRequest(letter);
            fail("Expected FormValidationException");
        } catch (FormValidationException ex) {
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_ID_FIELD, Constants.ELTR_ID_NON_NUMERIC));
        }
    }

    @Test
    public void validateRequestEmptyToRecipientsWithSentStatusTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
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
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
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
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setApplID("MyNSF");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
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
    public void validateRequestIllegalCharactersDraftTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("©");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setApplID("MyNSF");
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
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.MAIL_SUBJECT_FIELD),
                    new BaseError(Constants.ELTR_CONTENT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.ELTR_CONTENT_FIELD)
            );

        }
    }

    @Test
    public void validateRequestIllegalCharactersSentTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("©");
        letter.setEltrStatus(LetterStatus.Sent);
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setApplID("MyNSF");
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
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.MAIL_SUBJECT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.MAIL_SUBJECT_FIELD),
                    new BaseError(Constants.ELTR_CONTENT_FIELD, Constants.UNSUPPORTED_CHARACTERS_FIELD + Constants.ELTR_CONTENT_FIELD)
            );

        }
    }

    @Test
    public void validateRequestEmptySearchParametersTest() throws FormValidationException {
        Letter letter = new Letter();
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
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
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setApplID("MyNSF");
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
        letter.setEltrID(TestConstants.TEST_ELTR_ID);
        letter.setEltrContent("Test Content");
        letter.setEltrStatus(LetterStatus.Draft);
        letter.setEltrStatusUser("MyNSF");
        letter.setPlainText(false);
        letter.setApplID("MyNSF");
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


}
