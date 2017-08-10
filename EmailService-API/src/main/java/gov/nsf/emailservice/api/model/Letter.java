package gov.nsf.emailservice.api.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Letter POJO
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"eltrID", "eltrContent", "eltrStatus", "eltrStatusUser", "eltrStatusDate", "plainText", "tmplID", "emailInfo", "searchParameters", "applID"})
public class Letter {

    private String eltrID;
    private String eltrContent;
    private LetterStatus eltrStatus;
    private String eltrStatusUser;
    private String eltrStatusDate;
    private boolean plainText;
    private String tmplID;
    private EmailInfo emailInfo;
    private List<SearchParameter> searchParameters;
    private String applID;

    public Letter(String id) {
        setEltrID(id);
    }

    public Letter(){

    }

    public String getEltrID() {
        return eltrID;
    }

    public void setEltrID(String eltrID) {
        this.eltrID = eltrID;
    }

    public String getEltrContent() {
        return eltrContent;
    }

    public void setEltrContent(String eltrContent) {
        this.eltrContent = eltrContent;
    }

    public LetterStatus getEltrStatus() {
        return eltrStatus;
    }

    public void setEltrStatus(LetterStatus eltrStatus) {
        this.eltrStatus = eltrStatus;
    }

    public String getEltrStatusUser() {
        return eltrStatusUser;
    }

    public void setEltrStatusUser(String eltrStatusUser) {
        this.eltrStatusUser = eltrStatusUser;
    }

    public String getEltrStatusDate() {
        return eltrStatusDate;
    }

    public void setEltrStatusDate(String eltrStatusDate) {
        this.eltrStatusDate = eltrStatusDate;
    }

    public boolean isPlainText() {
        return plainText;
    }

    public void setPlainText(boolean plainText) {
        this.plainText = plainText;
    }

    public String getTmplID() {
        return tmplID;
    }

    public void setTmplID(String tmplID) {
        this.tmplID = tmplID;
    }

    public EmailInfo getEmailInfo() {
        return emailInfo;
    }

    public void setEmailInfo(EmailInfo emailInfo) {
        this.emailInfo = emailInfo;
    }

    public List<SearchParameter> getSearchParameters(){
        return searchParameters;
    }

    public void setSearchParameters(List<SearchParameter> searchParameters){
        this.searchParameters = searchParameters;
    }
    public String getApplID() { return applID; }

    public void setApplID(String applID) { this.applID = applID; }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("eltrID=" + eltrID + ", ");
        sb.append("eltrContent=" + eltrContent + ", ");
        sb.append("eltrStatus=" + eltrStatus + ", ");
        sb.append("eltrStatusDate=" + eltrStatusDate + ", ");
        sb.append("eltrStatusUser=" + eltrStatusUser + ", ");
        sb.append("plainText=" + plainText + ", ");
        sb.append("applID=" + applID + ", ");
        sb.append("emailInfo=" + emailInfo.toString() + ", ");
        sb.append("searchParameters=" + searchParameters + "}");


        return sb.toString();
    }

}
