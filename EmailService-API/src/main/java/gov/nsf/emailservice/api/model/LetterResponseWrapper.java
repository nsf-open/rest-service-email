package gov.nsf.emailservice.api.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gov.nsf.common.model.BaseResponseWrapper;

import javax.annotation.Generated;

/**
 * Letter response wrapper - extends BaseResponseWrapper
 *
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"letter"})
public class LetterResponseWrapper extends BaseResponseWrapper {


    private Letter letter;

    public LetterResponseWrapper(Letter letter){
        super();
        this.letter = letter;
    }

    public LetterResponseWrapper(){
        super();
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {
        this.letter = letter;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Letter : " + letter);
        sb.append("\n");

        return sb.toString();

    }

}
