package gov.nsf.emailservice.api.model;

import gov.mynsf.common.email.model.EmailRequest;
import gov.nsf.common.model.BaseResponseWrapper;

/**
 * EmailResponseWrapper
 *
 */
public class EmailResponseWrapper extends BaseResponseWrapper {

    private EmailRequest emailRequest;

    public EmailResponseWrapper(EmailRequest emailRequest){
        super();
        this.emailRequest = emailRequest;
    }

    public EmailResponseWrapper(){
        super();
    }

    public EmailRequest getEmailRequest() {
        return emailRequest;
    }

    public void setEmailRequest(EmailRequest emailRequest) {
        this.emailRequest = emailRequest;
    }
/*
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("EmailRequest : " + emailRequest);
        sb.append("\n");

        return sb.toString();

    }
*/
}
