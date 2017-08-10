package gov.nsf.emailservice.common.util;

import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendMetaData;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jacklinden on 10/25/16.
 */
public class Utils {
    private static Map<String,Boolean> stringsToBooleans = new HashMap<String,Boolean>();
    static{
        stringsToBooleans.put("y", true);
        stringsToBooleans.put("yes", true);
        stringsToBooleans.put("true", true);
        stringsToBooleans.put("n", false);
        stringsToBooleans.put("no", false);
        stringsToBooleans.put("false", false);
    }

    /**
     * Converts various String boolean representations to the corresponding boolean value
     *
     * @param s
     * @return
     */
    public static boolean convertStringToBoolean(String s){
        String lowerCase = s.toLowerCase();
        if(!stringsToBooleans.containsKey(lowerCase)){
            throw new IllegalArgumentException("Could not convert string: " + s + " to a boolean value");
        }

        return stringsToBooleans.get(lowerCase);
    }

    /**
     * Returns "Y" for true and "N" for false
     *
     * @param val
     * @return
     */
    public static String convertBooleanToString(boolean val){
        return val ? Constants.YES : Constants.NO;
    }


    public static EmailRequest convertLetterToEmailRequest(Letter letter, SendMetaData metaData){
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setBody(letter.getEltrContent());
        emailRequest.setSubject(letter.getEmailInfo().getMailSubject());
        emailRequest.setFrom(letter.getEmailInfo().getFromAddress());
        emailRequest.setTo(letter.getEmailInfo().getToAddresses());
        emailRequest.setCc(letter.getEmailInfo().getCcAddresses());
        emailRequest.setBcc(letter.getEmailInfo().getBccAddresses());
        emailRequest.setEncoding("UTF-8");
        emailRequest.setPlainText(letter.isPlainText());
        emailRequest.setSendMetaData(metaData);


        return emailRequest;
    }

    public static List<SearchParameter> getSearchParametersFromMap(Map<String,String> parameterMap){
        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();
        for(String paramName : parameterMap.keySet()){
            searchParameters.add(new SearchParameter(paramName, parameterMap.get(paramName)));
        }

        return searchParameters;
    }
}
