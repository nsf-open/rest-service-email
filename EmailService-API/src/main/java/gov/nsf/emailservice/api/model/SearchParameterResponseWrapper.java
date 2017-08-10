package gov.nsf.emailservice.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gov.nsf.common.model.BaseResponseWrapper;

import javax.annotation.Generated;
import java.util.List;

/**
 * Response wrapper for List of SearchParameter objects
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({"searchParameters"})
public class SearchParameterResponseWrapper extends BaseResponseWrapper{

    private List<SearchParameter> searchParameters;

    public SearchParameterResponseWrapper(){

    }

    public SearchParameterResponseWrapper(List<SearchParameter> searchParameters){
        setSearchParameters(searchParameters);
    }

    public List<SearchParameter> getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(List<SearchParameter> searchParameters) {
        this.searchParameters = searchParameters;
    }
}
