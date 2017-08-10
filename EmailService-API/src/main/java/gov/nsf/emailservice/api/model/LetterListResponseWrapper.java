package gov.nsf.emailservice.api.model;

import gov.nsf.common.model.BaseResponseWrapper;

import java.util.List;

/**
 * Response wrapper for Lists of Letter objects
 */
public class LetterListResponseWrapper extends BaseResponseWrapper{

    private List<Letter> letters;

    public LetterListResponseWrapper(List<Letter> letters){
        super();
        this.letters = letters;
    }

    public LetterListResponseWrapper(){
        super();
    }

    public List<Letter> getLetters() {
        return letters;
    }

    public void setLetters(List<Letter> letters) {
        this.letters = letters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Letters : " + letters);
        sb.append("\n");

        return sb.toString();

    }
}
