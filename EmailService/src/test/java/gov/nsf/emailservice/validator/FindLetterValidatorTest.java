package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.model.BaseError;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.TestConstants;
import gov.nsf.emailservice.common.util.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * JUnit tests for the FindLetterValidator
 */
public class FindLetterValidatorTest {
    FindLetterValidator validator = new FindLetterValidator();

    /**
     * Tests that the happy path case throws no form validation exception
     *
     * @throws FormValidationException
     */
    @Test
    public void validateRequestHappyPath() throws FormValidationException {
        Letter letter = new Letter();
        List<SearchParameter> searchParameters = Collections.singletonList(new SearchParameter("Award_Id", "12345"));
        letter.setSearchParameters(searchParameters);
        validator.validateRequest(letter);
    }

    /**
     * Tests that a search parameter with a null key produces an error
     *
     * @throws FormValidationException
     */
    @Test
    public void validateRequestNullKeyTest() throws FormValidationException{
        Letter letter = new Letter(TestConstants.TEST_ELTR_ID);
        List<SearchParameter> searchParameters = Collections.singletonList(new SearchParameter(null, "12345"));
        letter.setSearchParameters(searchParameters);

        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.QUERY_PARAMETERS, Constants.INVALID_PARAMETER_KEY_OR_VALUE));
        }
    }

    /**
     * Tests that a search parameter with a null value produces an error
     * @throws FormValidationException
     */
    @Test
    public void validateRequestNullValueTest() throws FormValidationException{
        Letter letter = new Letter(TestConstants.TEST_ELTR_ID);
        List<SearchParameter> searchParameters = Collections.singletonList(new SearchParameter("Award_Id", null));
        letter.setSearchParameters(searchParameters);

        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.QUERY_PARAMETERS, Constants.INVALID_PARAMETER_KEY_OR_VALUE));
        }
    }

    /**
     * Tests that not passing a search parameter is passed produces an error
     * @throws FormValidationException
     */
    @Test
    public void validateRequestEmptySearchParametersListTest() throws FormValidationException{
        Letter letter = new Letter(TestConstants.TEST_ELTR_ID);
        List<SearchParameter> searchParameters = Collections.emptyList();
        letter.setSearchParameters(searchParameters);
        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.QUERY_PARAMETERS, Constants.INVALID_NUMBER_OF_SEARCH_PARAMETERS));
        }
    }

    /**
     * Tests that passing more than one search parameters produces an error
     *
     * @throws FormValidationException
     */
    @Test
    public void validateRequestMoreThanOneSearchParameterTest() throws FormValidationException{
        Letter letter = new Letter(TestConstants.TEST_ELTR_ID);
        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
        searchParameters.add(new SearchParameter("Award_Id", "123456"));
        searchParameters.add(new SearchParameter("Panel_Id", "424242"));
        letter.setSearchParameters(searchParameters);

        try{
            validator.validateRequest(letter);
            fail("Expected FormValidationException");

        } catch( FormValidationException ex ){
            assertEquals(1, ex.getValidationErrors().size());
            TestUtils.assertContains(ex.getValidationErrors(),
                    new BaseError(Constants.QUERY_PARAMETERS, Constants.INVALID_NUMBER_OF_SEARCH_PARAMETERS));
        }
    }
}
