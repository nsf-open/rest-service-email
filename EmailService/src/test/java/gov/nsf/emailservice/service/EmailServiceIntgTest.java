package gov.nsf.emailservice.service;

import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterResponseWrapper;
import gov.nsf.emailservice.common.util.TestUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by jacklinden on 2/2/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"Embedded-EmailServiceTest-Context.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EmailServiceIntgTest {


    @Autowired
    private EmailServiceImpl emailService;

    @Test
    public void getLetterTest() throws Exception {
        LetterResponseWrapper wrapper = emailService.getLetter("1");
        assertNotNull(wrapper);
        assertNotNull(wrapper.getLetter());
    }

    @Test
    public void saveLetterTest() throws Exception {
        LetterResponseWrapper savedWrapper = emailService.saveLetter(TestUtils.getMockLetter(null),null);
        assertNotNull(savedWrapper);
        Letter returnedLetter = savedWrapper.getLetter();
        assertNotNull(returnedLetter);
        assertNotNull(returnedLetter.getEltrID());

        LetterResponseWrapper getWrapper = emailService.getLetter(returnedLetter.getEltrID());
        assertNotNull(getWrapper);
        Letter savedLetter = getWrapper.getLetter();
        assertNotNull(savedLetter);
        assertNotNull(savedLetter.getEltrID());
        assertEquals(returnedLetter.getEltrID(), savedLetter.getEltrID());
        assertEquals(returnedLetter.getEltrContent(), savedLetter.getEltrContent());

        System.out.println(returnedLetter);
        System.out.println(savedLetter);
    }

    @Test
    public void updateLetterTest() throws Exception {
        LetterResponseWrapper savedWrapper = emailService.updateLetter(TestUtils.getMockLetter("1"));
        assertNotNull(savedWrapper);
        Letter returnedLetter = savedWrapper.getLetter();
        assertNotNull(returnedLetter);
        assertNotNull(returnedLetter.getEltrID());

        LetterResponseWrapper getWrapper = emailService.getLetter(returnedLetter.getEltrID());
        assertNotNull(getWrapper);
        Letter savedLetter = getWrapper.getLetter();
        assertNotNull(savedLetter);
        assertNotNull(savedLetter.getEltrID());
        assertEquals(returnedLetter.getEltrID(), savedLetter.getEltrID());
        assertEquals(returnedLetter.getEltrContent(), savedLetter.getEltrContent());

        System.out.println(returnedLetter);
        System.out.println(savedLetter);
    }


}
