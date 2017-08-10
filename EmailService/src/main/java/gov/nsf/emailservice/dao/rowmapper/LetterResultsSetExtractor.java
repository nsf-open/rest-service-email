package gov.nsf.emailservice.dao.rowmapper;

import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.emailservice.api.model.EmailInfo;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.LetterStatus;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * LetterResultsSetExtractor for extracting Letter objects from DB
 *
 */
public class LetterResultsSetExtractor implements ResultSetExtractor<Letter> {

    /**
     * Returns a Letter object instantiated form the SQL results set
     *
     * @param rs
     * @return Letter
     * @throws SQLException
     * @throws DataAccessException
     */
    @Override
    public Letter extractData(ResultSet rs) throws SQLException {

        Letter letter = null;
        EmailInfo emailInfo = null;

        while(rs.next() ){
            if (letter == null) {
                letter = new Letter();

                letter.setEltrID(rs.getString(Constants.ELTR_ID) != null ? rs.getString(Constants.ELTR_ID).trim() : "");
                letter.setEltrContent(rs.getString(Constants.ELTR_CONTENT)!= null ? rs.getString(Constants.ELTR_CONTENT).trim() : "");
                letter.setEltrStatus(LetterStatus.getStatusFromCode(rs.getString(Constants.ELTR_STATUS).trim()));
                letter.setEltrStatusUser(rs.getString(Constants.ELTR_USER) != null ? rs.getString(Constants.ELTR_USER).trim() : "");
                letter.setEltrStatusDate(rs.getString(Constants.ELTR_DATE)!= null ? rs.getString(Constants.ELTR_DATE).trim() : "");
                letter.setPlainText(Utils.convertStringToBoolean(rs.getString(Constants.PLAIN_TEXT)));
                letter.setApplID(rs.getString(Constants.APPL_ID)!= null ? rs.getString(Constants.APPL_ID) : "");
                letter.setTmplID(rs.getString(Constants.TMPL_ID)!= null ? rs.getString(Constants.TMPL_ID) : null);
                emailInfo = new EmailInfo();
                emailInfo.setMailSubject(rs.getString(Constants.MAIL_SUBJECT)!= null ? rs.getString(Constants.MAIL_SUBJECT).trim() : "");
                emailInfo.setToAddresses(new ArrayList<String>());
                emailInfo.setCcAddresses(new ArrayList<String>());
                emailInfo.setBccAddresses(new ArrayList<String>());
                letter.setEmailInfo(emailInfo);
            }
            String recipientTypeCode = rs.getString(Constants.RECIPIENT_TYPE_CODE) != null ? rs.getString(Constants.RECIPIENT_TYPE_CODE).trim() : "";
            String mailAddress = rs.getString(Constants.MAIL_ADDRESS)!= null ? rs.getString(Constants.MAIL_ADDRESS).trim() : "";

            setMailRecipient(emailInfo,recipientTypeCode,mailAddress);

        }
        return letter;
    }

    private static void setMailRecipient(EmailInfo emailInfo, String recipientTypeCode, String mailAddress){
        if(recipientTypeCode.equals(Constants.TO)){
            emailInfo.getToAddresses().add(mailAddress);
        } else if(recipientTypeCode.equals(Constants.CC)){
            emailInfo.getCcAddresses().add(mailAddress);
        } else if(recipientTypeCode.equals(Constants.BCC)){
            emailInfo.getBccAddresses().add(mailAddress);
        } else {
            emailInfo.setFromAddress(mailAddress);
        }
    }
}

