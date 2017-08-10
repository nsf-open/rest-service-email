package gov.nsf.emailservice.api.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import gov.mynsf.common.email.model.SendMetaData;
import gov.nsf.emailservice.api.model.Letter;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
public class SendLetterRequest {

    private Letter letter;
    private SendMetaData sendMetaData;

    public SendLetterRequest(){

    }
    public SendLetterRequest(SendMetaData sendMetaData) {
        this.sendMetaData = sendMetaData;
    }

    public SendMetaData getSendMetaData() {
        return sendMetaData;
    }

    public void setSendMetaData(SendMetaData sendMetaData) {
        this.sendMetaData = sendMetaData;
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }
}
