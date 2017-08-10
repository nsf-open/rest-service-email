package gov.nsf.emailservice.controller;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendLevelEnum;
import gov.mynsf.common.email.model.SendMetaData;
import gov.mynsf.common.email.util.impl.NsfEmailUtilImpl;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.emailservice.dao.EmailDao;
import gov.nsf.emailservice.service.EmailServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.ResourceUtils;

import javax.mail.Message;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * EmailController JUnit tests
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:gov/nsf/emailservice/controller/send-email-controller-test-context.xml" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@WebAppConfiguration
public class SendEmailControllerTest {

    @Autowired
    JavaMailSender javaMailSender;  // standard javaMailSender

    private GreenMail greenMail; // mock smtp server that listens on port 3025... pretty snazzy

    @InjectMocks
    private EmailController controller;

    @Mock
    private EmailDao emailDaoMock;

    @Spy
    private EmailServiceImpl emailService;

    private MockMvc mockMvc;

    private NsfEmailUtilImpl nsfEmailUtil;

    @Before
    public void startMailServer() {
        MockitoAnnotations.initMocks(this); // needed to get the autowire controller to work but still read spring context

        nsfEmailUtil = new NsfEmailUtilImpl(javaMailSender, "testfrom@nsfemail.gov");
        nsfEmailUtil.setEnvironment("TEST");  // will show up in debug statements
        nsfEmailUtil.setSendLevel(SendLevelEnum.DebugLevel);

        // Start the Mock SMTP Server
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();

        emailService.setNsfEmailUtil(nsfEmailUtil);
        emailService.setEmailDao(emailDaoMock);


        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @After
    public void stopMailServer() {
        greenMail.stop(); // Kills the server
    }


    @Test
    public void sendEmail_JSON_scenario1() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_1.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());
        assertTrue(greenMail.waitForIncomingEmail(5000, 1)); // check if we got an email
        Message[] messages = greenMail.getReceivedMessages();
        System.out.println(GreenMailUtil.getBody(messages[0]));

        ArgumentCaptor<Letter> letterCaptor = ArgumentCaptor.forClass(Letter.class);
        ArgumentCaptor<SendMetaData> sendMetaCaptor = ArgumentCaptor.forClass(SendMetaData.class);
        verify(emailService).sendLetter(letterCaptor.capture(), sendMetaCaptor.capture());

        // verify response 200
        assertEquals("Response Code", HttpServletResponse.SC_OK, response.getResponse().getStatus());
        assertTrue("No errors.", noErrorResponse(response.getResponse().getContentAsString()));

        verifyEmails(letterCaptor.getValue(),sendMetaCaptor.getValue(),nsfEmailUtil.getSendLevel(),messages);

    }

    @Test
    public void sendEmail_JSON_scenario3() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_3.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());
        assertTrue(greenMail.waitForIncomingEmail(5000, 1)); // check if we got an email
        Message[] messages = greenMail.getReceivedMessages();
        System.out.println(GreenMailUtil.getBody(messages[0]));

        ArgumentCaptor<Letter> letterCaptor = ArgumentCaptor.forClass(Letter.class);
        ArgumentCaptor<SendMetaData> sendMetaCaptor = ArgumentCaptor.forClass(SendMetaData.class);
        verify(emailService).sendLetter(letterCaptor.capture(), sendMetaCaptor.capture());

        // verify response 200
        assertEquals("Response Code", HttpServletResponse.SC_OK, response.getResponse().getStatus());

        assertTrue("No errors.", noErrorResponse(response.getResponse().getContentAsString()));


    }

    private static boolean noErrorResponse(String jsonResp){
        return "{\"baseResponseWrapper\":{\"errors\":[],\"warnings\":[],\"informationals\":[]}}".equals(jsonResp);
    }

    @Test
    public void sendEmail_JSON_scenario7() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_7.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.emailInfo.toAddresses\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"},{\"fieldId\":\"letter.emailInfo.ccAddresses\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"},{\"fieldId\":\"letter.emailInfo.bccAddresses\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"},{\"fieldId\":\"sendMetaData.debugRecipients\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"},{\"fieldId\":\"sendMetaData.defaultBccRecipients\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"},{\"fieldId\":\"sendMetaData.prodSupportRecipients\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }

    @Test
    public void sendEmail_JSON_scenario9() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_9.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.emailInfo.toAddresses\",\"detail\":\"Error sending letter - Empty String submitted in recipient field(s)\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }

    @Test
    public void sendEmail_JSON_scenario10() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_10.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.emailInfo.toAddresses\",\"detail\":\"Error sending letter - Missing required field(s): To Email Address\"}],\"warnings\":[],\"informationals\":[]}}";


        assertEquals(expected, response.getResponse().getContentAsString());

    }

    /**
     * Empty From
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario11() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_11.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\" letter.emailInfo.fromAddress\",\"detail\":\"Error sending letter - Missing required field(s): Sender Email Address\"}],\"warnings\":[],\"informationals\":[]}}";


        assertEquals(expected, response.getResponse().getContentAsString());

    }

    /**
     * Invalid From
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario11B() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_11-b.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\" letter.emailInfo.fromAddress\",\"detail\":\"Error sending letter - Invalid e-mail address format.\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }


    @Test
    public void sendEmail_JSON_scenario12() throws Exception {

        // subject field empty
        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_12.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.emailInfo.mailSubject\",\"detail\":\"Error sending letter - Missing required field(s): Mail subject\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }

    @Test
    public void sendEmail_JSON_scenario13() throws Exception {

        // body field empty
        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_13.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.eltrContent\",\"detail\":\"Error sending letter - Missing required field(s): Mail Body\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }

    @Test
    public void sendEmail_JSON_scenario14() throws Exception {

        // invalid to field
        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_14.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.emailInfo.toAddresses\",\"detail\":\"Error sending letter - Invalid e-mail address format.\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }

    /**
     * Bad parameter test
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario16() throws Exception {


        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_16.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());

        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"letter.eltrContent\",\"detail\":\"Error sending letter - Missing required field(s): Mail Body\"}],\"warnings\":[],\"informationals\":[]}}";

        assertEquals(expected, response.getResponse().getContentAsString());

    }

    /**
     * PROD test
     *
     * happy path
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario18() throws Exception {

        // setting prod level
        nsfEmailUtil.setSendLevel(SendLevelEnum.ProdLevel);

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_18.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());
        assertTrue(greenMail.waitForIncomingEmail(5000, 1)); // check if we got an email
        Message[] messages = greenMail.getReceivedMessages();
        System.out.println(GreenMailUtil.getBody(messages[0]));

        ArgumentCaptor<Letter> letterCaptor = ArgumentCaptor.forClass(Letter.class);
        ArgumentCaptor<SendMetaData> sendMetaCaptor = ArgumentCaptor.forClass(SendMetaData.class);
        verify(emailService).sendLetter(letterCaptor.capture(), sendMetaCaptor.capture());

        // verify response 200
        assertEquals("Response Code", HttpServletResponse.SC_OK, response.getResponse().getStatus());
        assertTrue("No errors.", noErrorResponse(response.getResponse().getContentAsString()));

        verifyEmails(letterCaptor.getValue(),sendMetaCaptor.getValue(),nsfEmailUtil.getSendLevel(),messages);

    }

    /**
     * PROD test
     *
     * multiple address fields
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario19() throws Exception {

        // setting prod level
        nsfEmailUtil.setSendLevel(SendLevelEnum.ProdLevel);

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_19.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());
        assertTrue(greenMail.waitForIncomingEmail(5000, 1)); // check if we got an email
        Message[] messages = greenMail.getReceivedMessages();
        System.out.println(GreenMailUtil.getBody(messages[0]));

        ArgumentCaptor<Letter> letterCaptor = ArgumentCaptor.forClass(Letter.class);
        ArgumentCaptor<SendMetaData> sendMetaCaptor = ArgumentCaptor.forClass(SendMetaData.class);
        verify(emailService).sendLetter(letterCaptor.capture(), sendMetaCaptor.capture());

        // verify response 200
        assertEquals("Response Code", HttpServletResponse.SC_OK, response.getResponse().getStatus());
        assertTrue("No errors.", noErrorResponse(response.getResponse().getContentAsString()));

        verifyEmails(letterCaptor.getValue(),sendMetaCaptor.getValue(),nsfEmailUtil.getSendLevel(),messages);

    }

    /**
     * PROD test
     *
     * invalid prod support recipient
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario22() throws Exception {

        // setting prod level
        nsfEmailUtil.setSendLevel(SendLevelEnum.ProdLevel);

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_22.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());
        assertFalse("No email should be sent",greenMail.waitForIncomingEmail(1000, 1)); // check if we got an email


        // verify response 400
        assertEquals("Response Code", HttpServletResponse.SC_BAD_REQUEST, response.getResponse().getStatus());

        String expected = "{\"baseResponseWrapper\":{\"errors\":[{\"fieldId\":\"sendMetaData.prodSupportRecipients\",\"detail\":\"Error sending letter - Invalid e-mail address format.\"}],\"warnings\":[],\"informationals\":[]}}";
        assertEquals(expected, response.getResponse().getContentAsString());


    }

    /**
     * Test to make sure content type is getting changed to plain/text.
     * @throws Exception
     */
    @Test
    public void sendEmail_JSON_scenario26() throws Exception {

        String body = loadFile("classpath:gov/nsf/emailservice/controller/json/postletter_26.json");

        MvcResult response = mockMvc.perform(post("/sendletter").content(body).contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn();
        System.out.println(response.getResponse().getContentAsString());
        assertTrue(greenMail.waitForIncomingEmail(5000, 1)); // check if we got an email
        Message[] messages = greenMail.getReceivedMessages();
        System.out.println(GreenMailUtil.getBody(messages[0]));

        ArgumentCaptor<Letter> letterCaptor = ArgumentCaptor.forClass(Letter.class);
        ArgumentCaptor<SendMetaData> sendMetaCaptor = ArgumentCaptor.forClass(SendMetaData.class);
        verify(emailService).sendLetter(letterCaptor.capture(), sendMetaCaptor.capture());

        String[] contentType = messages[0].getHeader("Content-Type");

        Letter ltr = letterCaptor.getValue();
        SendMetaData metaData = sendMetaCaptor.getValue();


        // check that the content type is text/plain
        assertEquals("Content type should be text/plain", "text/plain; charset=utf8", contentType[0]);

        // verify response 200
        assertEquals("Response Code", HttpServletResponse.SC_OK, response.getResponse().getStatus());

        assertTrue("No errors.", noErrorResponse(response.getResponse().getContentAsString()));


    }

    /**
     * Helper method to verify based on the different sendLevels
     * @param sentLetter
     * @param sendLevel
     * @param messages
     * @throws Exception
     */
    private void verifyEmails(Letter sentLetter, SendMetaData metaData, SendLevelEnum sendLevel, Message[] messages) throws Exception{

        EmailRequest emailRequest = Utils.convertLetterToEmailRequest(sentLetter,metaData);

        switch (sendLevel)
        {
            case ProdLevel:
                verifyProd(emailRequest,messages);
                break;
            case LogLevel:
                break;
            case DebugLevel:
                verifyDebug(emailRequest, messages);
                break;

        }
    }

    /**
     * Internal helper to test Prod level emails
     * @param sentRequest
     * @param messages
     * @throws Exception
     */
    private void verifyProd(EmailRequest sentRequest,  Message[] messages) throws Exception{
        // Collect general count information
        int toCount = sentRequest.getToArray() == null ? 0 : sentRequest.getToArray().length;
        int ccCount = sentRequest.getCcArray() == null ? 0 : sentRequest.getCcArray().length;
        int bccCount = sentRequest.getBccArray() == null ? 0 : sentRequest.getBccArray().length;

        if(sentRequest.getSendMetaData() != null && sentRequest.getSendMetaData().getDefaultBccRecipients() != null){
            bccCount += sentRequest.getSendMetaData().getDefaultBccRecipients().size();
        } else if(nsfEmailUtil.getDefaultBccRecipient() != null){
            bccCount++;
        }
        int totalCount = toCount + ccCount + bccCount;

        assertEquals(totalCount, messages.length);


        int bccOnlyCount = 0;
        for(Message message : messages) {
            assertEquals(sentRequest.getSubject(), message.getSubject());  // confirm subjects match

            // confirm Bodies match
            String expectedBody = sentRequest.getBody();
            String actualBody = GreenMailUtil.getBody(message);

            // need to remove any non-alpha characters
            // need to remove any CR characters
            expectedBody = StringUtils.replaceChars(expectedBody, "\r", "");
            actualBody = StringUtils.replaceChars(actualBody, "\r", "");


            assertEquals(expectedBody.trim(),actualBody.trim());

            assertFalse("Prod Emails should never contain debug information", actualBody.contains("Intended TO Recipients"));

            if(message.getAllRecipients() == null ){
                bccCount++;
            } else {
                String expectedTo = getAddressList(sentRequest.getToArray());
                String actualTo = GreenMailUtil.getAddressList(message.getRecipients(Message.RecipientType.TO));

                assertEquals(expectedTo, actualTo);

                String expectedCC = getAddressList(sentRequest.getCcArray());
                String actualCC = GreenMailUtil.getAddressList(message.getRecipients(Message.RecipientType.CC));

                assertEquals(expectedCC, actualCC);
            }

        }

        // if you send an email with ONLY a BCC (no to, no cc) you test with this.
        if(bccOnlyCount > 0){
            assertEquals(bccCount, bccOnlyCount);
        }

    }

    /*
    private void verifyContentType(Message message, EmailRequest emailRequest){
        try {
            if (emailRequest.getEncoding() != null && !emailRequest.getEncoding().isEmpty()) {
                assertSame("Content types did not match",emailRequest.getEncoding(), message.getContentType());
            }
        }catch (MessagingException mex){
            assertTrue("Could not get content type", false);
        }
    }
*/



    /**
     * Internal helper to test Debug level emails
     * @param sentRequest
     * @param messages
     * @throws Exception
     */
    private void verifyDebug(EmailRequest sentRequest, Message[] messages) throws Exception{


        // since this is a debug message, we should only send out emails to the debug recipients.
        // check ths counts
        assertEquals(sentRequest.getSendMetaData().getDebugRecipients().size(), messages.length); // for logging we should only have 1 to address

        //Message message = messages[0];
        for(Message message : messages) {
            assertEquals("[TESTING -TEST]" + sentRequest.getSubject(), message.getSubject());

            //verifyContentType(message,sentRequest);

            // Check Body
            String expectedBody = sentRequest.getBody() + nsfEmailUtil.getDebugBodyHeader(sentRequest);
            String actualBody = GreenMailUtil.getBody(message);

            // need to remove any CR characters
            expectedBody = StringUtils.replaceChars(expectedBody, "\r", "");
            actualBody = StringUtils.replaceChars(actualBody, "\r", "");

            // date sent should not be check
            String dateSentPattern = "DateSent:.*<br>";
            expectedBody = StringUtils.removePattern(expectedBody, dateSentPattern);
            actualBody = StringUtils.removePattern(actualBody, dateSentPattern);

            expectedBody = StringUtils.trim(expectedBody);
            actualBody = StringUtils.trim(actualBody);


            assertEquals("Email Bodies should match. ",expectedBody, actualBody);

            // Debug to should be the debug recipient
            String expectedTo;
            if (sentRequest.getSendMetaData() != null && sentRequest.getSendMetaData().getDebugRecipients() != null
                    && !sentRequest.getSendMetaData().getDebugRecipients().isEmpty()) {
                expectedTo = getAddressList(sentRequest.getSendMetaData().getDebugRecipients().toArray());
            } else {
                expectedTo = nsfEmailUtil.getDebugRecipient();
            }
            String actualTo = GreenMailUtil.getAddressList(message.getRecipients(Message.RecipientType.TO));

            assertEquals(expectedTo, actualTo);

            // cc should be null
            String expectedCC = null;
            String actualCC = GreenMailUtil.getAddressList(message.getRecipients(Message.RecipientType.CC));

            assertEquals(expectedCC, actualCC);

            // bcc should be null
            String expectedBCC = null;
            String actualBCC = GreenMailUtil.getAddressList(message.getRecipients(Message.RecipientType.BCC));

            assertEquals(expectedBCC, actualBCC);
        }

    }

    /**
     * helper to convert an array of addresses to a comma string.
     * @param addresses
     * @return
     */
    private static String getAddressList(Object[] addresses) {
        if(null == addresses) {
            return null;
        } else {
            StringBuilder ret = new StringBuilder();

            for(int i = 0; i < addresses.length; ++i) {
                if(i > 0) {
                    ret.append(", ");
                }

                ret.append(addresses[i].toString());
            }

            return ret.toString();
        }
    }


    private static String loadFile(String filePath) throws FileNotFoundException, IOException {
        File file = ResourceUtils.getFile(filePath);
        byte[] data = Files.readAllBytes(file.toPath());

        return new String(data);
    }

}
