package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseError;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit tests for GetLetterValidator
 */
public class GetLetterValidatorTest{
    private GetLetterValidator validator = new GetLetterValidator();


    /**
     * Tests that the happy path input produces no errors
     *
     * @throws FormValidationException
     */
    @Test
    public void validateRequestHappyPath() throws FormValidationException{
        Letter letter = new Letter(TestConstants.TEST_ELTR_ID);
        validator.validateRequest(letter);
    }

    /**
     * Tests that passing a null eltrID produces an error
     *
     * @throws FormValidationException
     */
    @Test
    public void validateRequestNullIdTest() throws FormValidationException{
        Letter letter = new Letter(null);

        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_ID_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_ID_FIELD));
        }
    }

    /**
     * Tests that passing an empty string eltrID produces an error
     * @throws FormValidationException
     */
    @Test
    public void validateRequestEmptyStringIDTest() throws FormValidationException{
        Letter letter = new Letter("");

        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_ID_FIELD, Constants.MISSING_NULL_EMPTY_FIELD + Constants.ELTR_ID_FIELD));
        }
    }

    /**
     * Tests that passing a non-integer eltrID produces an error
     *
     * @throws FormValidationException
     */
    @Test
    public void validateRequestNonIntegerIDTest() throws FormValidationException{
        Letter letter = new Letter("fish");

        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.ELTR_ID_FIELD, Constants.ELTR_ID_NON_NUMERIC));
        }
    }


}
