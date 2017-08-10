package gov.nsf.emailservice.validator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * EmailServiceValidatorFactory JUnit tests
 */
@RunWith(MockitoJUnitRunner.class)
public class EmailServiceValidatorFactoryTest {

    @InjectMocks
    EmailServiceValidatorFactory factory;

    @Mock
    CreateLetterValidator createLetterValidator;

    @Before
    public void setup(){
        assertNotNull(factory);
        assertNotNull(factory.getCreateLetterValidator());
    }
    @Test
    public void getLetterValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("getLetter");
        assertTrue(validator instanceof GetLetterValidator);
    }

    @Test
    public void createLetterValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("createLetter");
        assertTrue(validator instanceof CreateLetterValidator);
    }

    @Test
    public void findLetterValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("findLetter");
        assertTrue(validator instanceof FindLetterValidator);
    }

    @Test
    public void updateLetterValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("updateLetter");
        assertTrue(validator instanceof UpdateLetterValidator);
    }

    @Test
    public void deleteLetterValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("deleteLetter");
        assertTrue(validator instanceof GetLetterValidator);
    }

    @Test
    public void getSearchParametersValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("getSearchParameters");
        assertTrue(validator instanceof GetLetterValidator);
    }

    @Test
    public void nonExistantValidatorTest(){
        EmailServiceValidator validator = factory.getValidator("LOL");
        assertNull(validator);
    }

}
