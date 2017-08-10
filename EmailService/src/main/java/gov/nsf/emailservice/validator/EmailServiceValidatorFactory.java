package gov.nsf.emailservice.validator;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jacklinden on 11/7/16.
 */
public class EmailServiceValidatorFactory {

    @Autowired
    private CreateLetterValidator createLetterValidator;

    /**
     * CreateLetterValidator getter
     *
     * @return
     */
    public CreateLetterValidator getCreateLetterValidator() {
        return createLetterValidator;
    }

    /**
     * CreateLetterValidator setter
     * @param createLetterValidator
     */
    public void setCreateLetterValidator(CreateLetterValidator createLetterValidator) {
        this.createLetterValidator = createLetterValidator;
    }

    /**
     * Returns the validator for the given serviceMethod
     *
     * @param serviceMethod
     * @return
     */
    public EmailServiceValidator getValidator(String serviceMethod){
        EmailServiceValidator validator = null;

        if(("getLetter").equals(serviceMethod)){
            validator = new GetLetterValidator();
        } else if(("findLetter").equals(serviceMethod)){
            validator = new FindLetterValidator();
        } else if(("createLetter").equals(serviceMethod)){
            validator = createLetterValidator;
        } else if(("updateLetter").equals(serviceMethod)){
            validator = new UpdateLetterValidator();
        } else if(("getSearchParameters").equals(serviceMethod)){
            validator = new GetLetterValidator();
        } else if(("deleteLetter").equals(serviceMethod)){
            validator = new GetLetterValidator();
        } else {
            return null;
        }


        return validator;
    }
}
