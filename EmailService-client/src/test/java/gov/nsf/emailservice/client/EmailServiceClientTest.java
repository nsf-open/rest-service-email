package gov.nsf.emailservice.client;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.mynsf.common.email.model.SendMetaData;
import gov.nsf.common.model.BaseResponseWrapper;
import gov.nsf.emailservice.api.model.EmailInfo;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterResponseWrapper;

/**
 *
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
public class EmailServiceClientTest {

    @Test
    public void test(){}

}
