package gov.nsf.emailservice.validator;

import gov.mynsf.common.email.model.EmailRequest;
import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.model.BaseError;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by amahabal on 12/7/16.
 */
public class SendLetterValidatorTest {

    /**
     * Tests that the validateEmailRequest method successfully validates the request object
     * <p>
     * Test must not encounter an exception to pass
     *
     * @throws FormValidationException
     */
    private SendLetterValidator send = new SendLetterValidator();


    @Test
    public void validateSendEmailRequestHappyPathTest() throws FormValidationException {

        EmailRequest mockedRequest = TestUtils.getMockEmailRequest();
        send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));


    }
    /**
     * Tests that the validateRequest method throws a FormValidationException containing
     * a List with  errors when a Request object Subject with null fields is passed
     *
     * Test expects a FormValidationException containing a List with  BaseErrors
     *
     * @throws FormValidationException
     */

    @Test
    public void validateSendEmailRequestValidateSubjectTest() throws FormValidationException {

        EmailRequest mockedRequest = TestUtils.getMockSubjectValidate();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.MAIL_SUBJECT_EMAIL_FIELD, Constants.MISSING_EMAIL_SUBJECT)));
        }


    }
    /**
     * Tests that the validateRequest method throws a FormValidationException containing
     * a List with  errors when a Request object Body with null fields is passed
     *
     * Test expects a FormValidationException containing a List with  BaseErrors
     *
     * @throws FormValidationException
     */
    @Test
    public void validateSendEmailRequestValidateBodyTest() throws FormValidationException {

        EmailRequest mockedRequest = TestUtils.getMockBodyValidate();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.MAIL_BODY_EMAIL_FIELD, Constants.MISSING_EMAIL_BODY)));
        }


    }

    /**
     * Tests that the validateRequest method throws a FormValidationException containing
     * a List with  errors when a Request object From field with null fields is passed
     *
     * Test expects a FormValidationException containing a List with  BaseErrors
     *
     * @throws FormValidationException
     */
    @Test
    public void validateSendEmailRequestValidateFromTest() throws FormValidationException {

        EmailRequest mockedRequest = TestUtils.getMockFromValidate();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.SENDER_EMAIL_ADDRESS_FIELD, Constants.MISSING_EMAIL_SENDER_ADDRESS)));
        }

    }

    /**
     * Tests that the validateRequest method throws a FormValidationException containing
     * a List with errors when a Request object with no to addresses are passed
     *
     * Test expects a FormValidationException containing a List with  BaseErrors
     *
     * @throws FormValidationException
     */
    @Test
    public void validateSendEmailRequestValidateToCcBccTest() throws FormValidationException {

        // to, cc, bcc are empty
        EmailRequest mockedRequest = TestUtils.getMock_Invalid_ToCcBcc_Request_1();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.TO_EMAIL_RECIPIENTS_FIELD, Constants.EMPTY_TO_EMAIL_RECIPIENTS)));
        }

        // cc, and bcc are not empty
        mockedRequest = TestUtils.getMock_Invalid_ToCcBcc_Request_2();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.TO_EMAIL_RECIPIENTS_FIELD, Constants.EMPTY_TO_EMAIL_RECIPIENTS)));
        }

        // to,cc,bcc are null
        mockedRequest = TestUtils.getMock_Invalid_ToCcBcc_Request_3();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.TO_EMAIL_RECIPIENTS_FIELD, Constants.EMPTY_TO_EMAIL_RECIPIENTS)));
        }

        // to has empty string
        mockedRequest = TestUtils.getMock_Invalid_ToCcBcc_Request_4();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 1);
            assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.TO_EMAIL_RECIPIENTS_FIELD, Constants.EMPTY_STRING_EMAIL_RECIPIENTS)));
        }

        // to has empty string
        mockedRequest = TestUtils.getMock_Invalid_ToCcBcc_Request_5();

        try{
            send.validateRequest(TestUtils.convertEmailRequestToSendLetterRequest(mockedRequest));
            fail("Expected test to throw FormValidationException");
        } catch( FormValidationException ex){
            assertTrue(ex.getValidationErrors().size()  == 3);
            //assertTrue(ex.getValidationErrors().get(0).equals( new BaseError(Constants.TO_EMAIL_RECIPIENTS_FIELD, Constants.EMPTY_STRING_EMAIL_RECIPIENTS)));
        }
    }
}


