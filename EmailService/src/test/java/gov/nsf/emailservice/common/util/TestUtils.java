package gov.nsf.emailservice.common.util;

import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendMetaData;
import gov.nsf.common.model.BaseError;
import gov.nsf.emailservice.api.model.EmailInfo;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.api.model.request.SendLetterRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.fail;

/**
 * Utility methods for test classes
 */
public class TestUtils {
    /**
     * Generates a test Letter object
     *
     * (objectID-templateTypeName) combination makes the Letter unique
     * @param id
     * @return Letter
     */
    public static Letter getMockLetter(String id) {

        Letter letter = new Letter();

        letter.setEltrID(id);
        letter.setEltrContent(TestConstants.TEST_ELTR_CONTENT);
        letter.setEltrStatus(TestConstants.TEST_ELTR_STATUS);
        letter.setEltrStatusUser(TestConstants.TEST_ELTR_USER);
        letter.setEltrStatusDate(Constants.CURRENT_DATE);
        letter.setApplID("42");
        letter.setPlainText(TestConstants.TRUE);


        EmailInfo info = new EmailInfo();
        info.setFromAddress(TestConstants.TEST_FROM_ADDRESS);
        info.setToAddresses(new ArrayList<String>());
        info.setCcAddresses(new ArrayList<String>());
        info.setBccAddresses(new ArrayList<String>());
        info.getToAddresses().add("test@nsf.gov");
        info.getToAddresses().add("test@nsf.gov");
        info.getCcAddresses().add("test@nsf.gov");
        info.getBccAddresses().add("test@nsf.gov");
        info.setMailSubject(TestConstants.TEST_MAIL_SUBJECT);

        letter.setEmailInfo(info);

        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
        searchParameters.add(new SearchParameter("award_id", "12345"));
        searchParameters.add(new SearchParameter("panel_id", "43234"));
        letter.setSearchParameters(searchParameters);
        return letter;
    }

    /**
     * Generates a Email Request object
     *
     * @return
     */
    public static EmailRequest getMockEmailRequest(){

    	EmailRequest req = new EmailRequest();
		req.setTo(new ArrayList<String>());
		req.getTo().add("test@nsf.gov");
		req.getTo().add("test@nsf.gov");
		req.setCc(new ArrayList<String>());
		req.setBcc(new ArrayList<String>());
		req.getCc().add("ktest@associatest.nsf.gov");
		req.getCc().add("test@nsf.gov");
		req.getBcc().add("test@nsf.gov");
		req.setFrom("test@nsf.gov");
		req.setSubject("Test mail util test mmm");
		req.setBody("This is a test email generated for testing");
		//req.setProdSupportEmail(new ArrayList<String>());
		//req.getProdSupportEmail().add("test@nsf.gov");
        SendMetaData metaData = new SendMetaData();
        metaData.setDebugRecipients(new ArrayList<String>());
        metaData.getDebugRecipients().add("test@nsf.gov");
        metaData.getDebugRecipients().add("test@nsf.gov");
        //private List<String> prodSupportRecipient;
        //private List<String> defaultBccRecipient;
        metaData.setProdSupportRecipients(new ArrayList<String>());
        metaData.getProdSupportRecipients().add("test@nsf.gov");
        metaData.setDefaultBccRecipients(new ArrayList<String>());
        metaData.getDefaultBccRecipients().add("test@nsf.gov");
        metaData.getDefaultBccRecipients().add("test@nsf.gov");
        req.setSendMetaData(metaData);
        return req;
    }


    public static EmailRequest getMockSubjectValidate(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        req.getTo().add("test@nsf.gov");
        req.getTo().add("test@nsf.gov");
        req.setCc(new ArrayList<String>());
        req.setBcc(new ArrayList<String>());
        req.getCc().add("ktest@associatest.nsf.gov");
        req.getCc().add("test@nsf.gov");
        req.getBcc().add("test@nsf.gov");
        req.setFrom("test@nsf.gov");
        //req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");
       // req.setProdSupportEmail(new ArrayList<String>());
        //req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }

    public static EmailRequest getMockBodyValidate(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        req.getTo().add("test@nsf.gov");
        req.getTo().add("test@nsf.gov");
        req.setCc(new ArrayList<String>());
        req.setBcc(new ArrayList<String>());
        req.getCc().add("ktest@associatest.nsf.gov");
        req.getCc().add("test@nsf.gov");
        req.getBcc().add("test@nsf.gov");
        req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        //req.setBody("This is a test email generated for testing");
       // req.setProdSupportEmail(new ArrayList<String>());
       // req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }

    public static EmailRequest getMockFromValidate(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        req.getTo().add("test@nsf.gov");
        req.getTo().add("test@nsf.gov");
        req.setCc(new ArrayList<String>());
        req.setBcc(new ArrayList<String>());
        req.getCc().add("ktest@associatest.nsf.gov");
        req.getCc().add("test@nsf.gov");
        req.getBcc().add("test@nsf.gov");
        //req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");
       // req.setProdSupportEmail(new ArrayList<String>());
       // req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }

    /**
     * This request has all empty to/cc/bcc
     * @return
     */
    public static EmailRequest getMock_Invalid_ToCcBcc_Request_1(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        //req.getTo().add("test@nsf.gov");
        //req.getTo().add("test@nsf.gov");
        req.setCc(new ArrayList<String>());
        req.setBcc(new ArrayList<String>());
        //req.getCc().add("ktest@associatest.nsf.gov");
        //req.getCc().add("test@nsf.gov");
        //req.getBcc().add("test@nsf.gov");
        req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");
       // req.setProdSupportEmail(new ArrayList<String>());
       // req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }

    /**
     * This request has all empty to but filled in bcc and cc
     * @return
     */
    public static EmailRequest getMock_Invalid_ToCcBcc_Request_2(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        //req.getTo().add("test@nsf.gov");
        //req.getTo().add("test@nsf.gov");
        req.setCc(new ArrayList<String>());
        req.setBcc(new ArrayList<String>());
        req.getCc().add("ktest@associatest.nsf.gov");
        //req.getCc().add("test@nsf.gov");
        req.getBcc().add("test@nsf.gov");
        req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");
        // req.setProdSupportEmail(new ArrayList<String>());
        // req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }

    /**
     * This request has all null to,cc,bcc
     * @return
     */
    public static EmailRequest getMock_Invalid_ToCcBcc_Request_3(){

        EmailRequest req = new EmailRequest();
        req.setTo(null);
        //req.getTo().add("test@nsf.gov");
        //req.getTo().add("test@nsf.gov");
        req.setCc(null);
        req.setBcc(null);
        //req.getCc().add("ktest@associatest.nsf.gov");
        //req.getCc().add("test@nsf.gov");
        //req.getBcc().add("test@nsf.gov");
        req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");
        // req.setProdSupportEmail(new ArrayList<String>());
        // req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }

    /**
     * There is an empty string in the to array
     * @return
     */
    public static EmailRequest getMock_Invalid_ToCcBcc_Request_4(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        req.getTo().add("");
        //req.getTo().add("test@nsf.gov");
        req.setCc(null);
        req.setBcc(null);
        //req.getCc().add("ktest@associatest.nsf.gov");
        //req.getCc().add("test@nsf.gov");
        //req.getBcc().add("test@nsf.gov");
        req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");
        // req.setProdSupportEmail(new ArrayList<String>());
        // req.getProdSupportEmail().add("test@nsf.gov");

        return req;
    }
    /**
     * There is an empty string in the to array
     * @return
     */
    public static EmailRequest getMock_Invalid_ToCcBcc_Request_5(){

        EmailRequest req = new EmailRequest();
        req.setTo(new ArrayList<String>());
        //req.getTo().add("");
        req.getTo().add("test@nsf.gov");
        req.setCc(null);
        req.setBcc(null);

        req.setFrom("test@nsf.gov");
        req.setSubject("Test mail util test mmm");
        req.setBody("This is a test email generated for testing");

        req.setSendMetaData(new SendMetaData());
        req.getSendMetaData().setDebugRecipients(Arrays.asList(""));
        req.getSendMetaData().setProdSupportRecipients(Arrays.asList(""));
        req.getSendMetaData().setDefaultBccRecipients(Arrays.asList(""));

        return req;
    }

    public static SendLetterRequest convertEmailRequestToSendLetterRequest(EmailRequest request){

        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setFromAddress(request.getFrom());
        emailInfo.setToAddresses(request.getTo());
        emailInfo.setCcAddresses(request.getCc());
        emailInfo.setBccAddresses(request.getBcc());
        emailInfo.setMailSubject(request.getSubject());

        Letter letter = new Letter();
        letter.setEltrContent(request.getBody());
        letter.setEmailInfo(emailInfo);

        SendLetterRequest letterRequest = new SendLetterRequest();
        letterRequest.setSendMetaData(request.getSendMetaData());
        letterRequest.setLetter(letter);

        return letterRequest;
    }

    public static void assertContains(List<BaseError> observedErrors, BaseError... expectedErrors){
        ArrayList<BaseError> _observedErrors = new ArrayList<BaseError>(observedErrors);
        for( BaseError expectedError : expectedErrors ){
            int indexFound = -1;
            for( int i = 0; i < _observedErrors.size(); i++ ){
                if( expectedError.equals(_observedErrors.get(i)) ){
                    indexFound = i;
                    break;
                }

            }
            if( indexFound < 0 ){
                fail("Expected " + expectedError + " to be in the errors list but was not");
            } else {
                _observedErrors.remove(indexFound);
            }
        }
    }

}
