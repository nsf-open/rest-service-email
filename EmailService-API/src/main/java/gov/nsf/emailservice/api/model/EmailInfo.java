package gov.nsf.emailservice.api.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"mailSubject", "fromAddress", "toAddresses", "ccAddresses", "bccAddresses"})
public class EmailInfo {

    private String mailSubject;
    private String fromAddress;
    private List<String> toAddresses;
    private List<String> ccAddresses;
    private List<String> bccAddresses;

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public List<String> getToAddresses() {
        return toAddresses;
    }

    public void setToAddresses(List<String> toAddresses) {
        this.toAddresses = toAddresses;
    }

    public List<String> getCcAddresses() {
        return ccAddresses;
    }

    public void setCcAddresses(List<String> ccAddresses) {
        this.ccAddresses = ccAddresses;
    }

    public List<String> getBccAddresses() {
        return bccAddresses;
    }

    public void setBccAddresses(List<String> bccAddresses) {
        this.bccAddresses = bccAddresses;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("mailSubject=" + mailSubject + ", ");
        sb.append("fromAddress=" + fromAddress + ", ");
        sb.append("toAddresses=" + toAddresses + ", ");
        sb.append("ccAddresses=" + ccAddresses + ", ");
        sb.append("bccAddresses=" + bccAddresses + "}");

        return sb.toString();
    }
}
