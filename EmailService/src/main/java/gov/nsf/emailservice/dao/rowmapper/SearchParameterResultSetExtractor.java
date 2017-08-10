package gov.nsf.emailservice.dao.rowmapper;

import gov.nsf.emailservice.api.model.SearchParameter;
import gov.nsf.emailservice.common.util.Constants;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ResultSetExtractor for List<SearchParameter>
 */
public class SearchParameterResultSetExtractor implements ResultSetExtractor<List<SearchParameter>> {

    /**
     * Returns a List<SearchParameter> instantiated form the SQL results set
     *
     * @param rs
     * @return List<SearchParameter>
     * @throws SQLException
     */
    @Override
    public List<SearchParameter> extractData(ResultSet rs) throws SQLException {

        List<SearchParameter> searchParameters = new ArrayList<SearchParameter>();

        while(rs.next() ){
            String eltrID = rs.getString(Constants.ELTR_ID) != null ? rs.getString(Constants.ELTR_ID).trim() : "";
            String key = rs.getString(Constants.ATR_NAME) != null ? rs.getString(Constants.ATR_NAME).trim() : "";
            String value = rs.getString(Constants.SEARCH_PARAMETER_VALUE) != null ? rs.getString(Constants.SEARCH_PARAMETER_VALUE).trim() : "";

            SearchParameter searchParameter = new SearchParameter();
            searchParameter.setKey(key);
            searchParameter.setValue(value);
            searchParameter.setId(eltrID + Constants.SEPARATOR + key);

            searchParameters.add(searchParameter);
        }
        return searchParameters;
    }
}
