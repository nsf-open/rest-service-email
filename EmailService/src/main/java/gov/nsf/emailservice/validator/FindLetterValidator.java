package gov.nsf.emailservice.validator;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.model.BaseError;
import gov.nsf.emailservice.api.model.Letter;
import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * FindLetterValidator class
 *
 */
public class FindLetterValidator extends EmailServiceValidator {

    /**
     * Validates the request
     *
     * @param letter
     * @throws FormValidationException
     */
    @Override
    public void validateRequest(Letter letter) throws FormValidationException {
        List<BaseError> errors = new ArrayList<BaseError>();

        errors.addAll(validateSearchParameters(letter.getSearchParameters()));

        if(errorOccurred(errors)){
            throw new FormValidationException(Constants.INVALID_FORM_DATA, errors);
        }
    }

    /**
     * Validates the search parameters
     *
     * @param searchParameters
     * @return
     */
    @Override
    protected List<BaseError> validateSearchParameters(List<SearchParameter> searchParameters){
        List<BaseError> errors = new ArrayList<BaseError>();

        if(searchParameters.size() != 1){
            errors.add(new BaseError(Constants.QUERY_PARAMETERS, Constants.INVALID_NUMBER_OF_SEARCH_PARAMETERS));
        } else {
            SearchParameter searchParameter = searchParameters.get(0);
            if (isInvalidString(searchParameter.getKey()) || isInvalidString(searchParameter.getValue())) {
                errors.add(new BaseError(Constants.QUERY_PARAMETERS, Constants.INVALID_PARAMETER_KEY_OR_VALUE));
            }
        }
        return errors;
    }
}
