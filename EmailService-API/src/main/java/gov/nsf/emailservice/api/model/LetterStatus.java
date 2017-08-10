package gov.nsf.emailservice.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * LetterStatus enum class
 */
public enum LetterStatus {
    Sent("S"),
    Draft("D"),
    Invalid("I");

    private static final Logger LOGGER = Logger.getLogger(LetterStatus.class);

    private final String statusCode;
    private static final Map<String,LetterStatus> valueToStatus = new HashMap<String,LetterStatus>();

    static{
        for(LetterStatus status : LetterStatus.values()){
            valueToStatus.put(status.getCode(), status);
        }
    }

    LetterStatus(String statusCode)  {

        this.statusCode = statusCode;

    }

    @JsonCreator
    public static LetterStatus forValue(String status){

        LetterStatus letterStatus = null;
        try{
            letterStatus = LetterStatus.valueOf(status);
        } catch( Exception e ){
            LOGGER.info(e);
            return LetterStatus.Invalid;
        }

        return letterStatus;
    }

    public String getCode() {
        return this.statusCode;
    }

    @JsonValue
    public static LetterStatus getStatusFromCode(String code){
        if(!valueToStatus.containsKey(code)){
            throw new IllegalArgumentException("The status code " + code + " does not map to a valid LetterStatus");
        }

        return valueToStatus.get(code);
    }
}
