package gov.nsf.emailservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.mynsf.common.email.model.EmailRequest;
import gov.mynsf.common.email.model.SendMetaData;
import gov.mynsf.common.restclient.NsfRestTemplate;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseResponseWrapper;
import gov.nsf.emailservice.api.model.*;
import gov.nsf.emailservice.api.model.request.SendLetterRequest;
import gov.nsf.emailservice.api.service.EmailService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by jacklinden on 11/18/16.
 */
public class EmailServiceClientImpl implements EmailService {

    private String emailServiceURL;
    private String emailServiceServiceUserName;
    private String emailServicePassword;
    private boolean authenticationRequired;
    private int requestTimeout;

    @Override
    public LetterResponseWrapper getLetter(String id) throws RollbackException {


        String requestUrl = emailServiceURL + "/letter/" + id + "/";
        String responseBody = sendRequest(requestUrl, HttpMethod.GET, null);

        LetterResponseWrapper wrapper = null;
        try {
            wrapper = (LetterResponseWrapper) extractWrapperResponse(responseBody, LetterResponseWrapper.class, "letterResponseWrapper");
        } catch (IOException e) {
            throw new RollbackException(e);
        }

        return wrapper;
    }

    @Override
    public LetterListResponseWrapper findLetter(SearchParameter searchParameter) throws RollbackException {
        String requestUrl = emailServiceURL + "/letter?" + searchParameter.getKey() + "=" + searchParameter.getValue();
        String responseBody = sendRequest(requestUrl, HttpMethod.GET, null);

        LetterListResponseWrapper wrapper = null;
        try {
            wrapper = (LetterListResponseWrapper) extractWrapperResponse(responseBody, LetterListResponseWrapper.class, "letterListResponseWrapper");
        } catch (IOException e) {
            throw new RollbackException(e);
        }

        return wrapper;
    }

    @Override
    public LetterResponseWrapper saveLetter(Letter letter) throws RollbackException {
        return saveLetter(letter, null);
    }

    @Override
    public LetterResponseWrapper saveLetter(Letter letter, String templateID) throws RollbackException {

        String jsonBody = null;
        try {
            jsonBody = new ObjectMapper().writeValueAsString(letter);
        } catch (JsonProcessingException e) {
            throw new RollbackException(e);
        }

        String requestUrl = (templateID == null ? emailServiceURL + "/letter" : emailServiceURL + "/letter?templateID=" + templateID + "/");
        String responseBody = sendRequest(requestUrl, HttpMethod.POST, jsonBody);
        LetterResponseWrapper wrapper = null;

        try {
            wrapper = (LetterResponseWrapper) extractWrapperResponse(responseBody, LetterResponseWrapper.class, "letterResponseWrapper");
        } catch (IOException e) {
            throw new RollbackException(e);
        }

        return wrapper;
    }

    @Override
    public LetterResponseWrapper updateLetter(Letter letter) throws RollbackException {

        String jsonBody = null;
        try {
            jsonBody = new ObjectMapper().writeValueAsString(letter);
        } catch (JsonProcessingException e) {
            throw new RollbackException(e);
        }


        String requestUrl = emailServiceURL + "/letter/" + letter.getEltrID()  + "/";
        String responseBody = sendRequest(requestUrl, HttpMethod.PUT, jsonBody);
        LetterResponseWrapper wrapper = null;

        try {
            wrapper = (LetterResponseWrapper) extractWrapperResponse(responseBody, LetterResponseWrapper.class, "letterResponseWrapper");
        } catch (IOException e) {
            throw new RollbackException(e);
        }

        return wrapper;
    }

    @Override
    public LetterResponseWrapper deleteLetter(String id) throws RollbackException {
        String requestUrl = emailServiceURL + "/letter/" + id + "/";
        String responseBody = sendRequest(requestUrl, HttpMethod.DELETE, null);

        LetterResponseWrapper wrapper = null;
        try {
            wrapper = (LetterResponseWrapper) extractWrapperResponse(responseBody, LetterResponseWrapper.class, "letterResponseWrapper");
        } catch (IOException e) {
            throw new RollbackException(e);
        }

        return wrapper;
    }

    @Override
    public SearchParameterResponseWrapper getSearchParameters(String eltrID) throws RollbackException {
        String requestUrl = emailServiceURL + "/letter/" + eltrID + "/searchparameters";
        String responseBody = sendRequest(requestUrl, HttpMethod.GET, null);

        SearchParameterResponseWrapper wrapper = null;
        try {
            wrapper = (SearchParameterResponseWrapper) extractWrapperResponse(responseBody, SearchParameterResponseWrapper.class, "letterResponseWrapper");
        } catch (IOException e) {
            throw new RollbackException(e);
        }

        return wrapper;
    }

    @Override
    public BaseResponseWrapper sendLetter(Letter letter, SendMetaData metaData) {

        String jsonBody = null;
        SendLetterRequest req = new SendLetterRequest();
        req.setLetter(letter);
        req.setSendMetaData(metaData);
        try {
            jsonBody = new ObjectMapper().writeValueAsString(req);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        String requestUrl = emailServiceURL + "/sendletter";

        BaseResponseWrapper wrapper = null;
        String responseBody = sendRequest(requestUrl, HttpMethod.POST, jsonBody);

        try {
            wrapper = extractWrapperResponse(responseBody, BaseResponseWrapper.class, "baseResponseWrapper");
        } catch (IOException e) {
            e.printStackTrace();
        }


        return wrapper;
    }


    private BaseResponseWrapper extractBaseResponse(String body)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body).findValue("baseResponseWrapper");
        BaseResponseWrapper wrapper = mapper.readValue(node.toString(), BaseResponseWrapper.class);
        return wrapper;
    }
    private LetterResponseWrapper extractLetterResponse(String body)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body).findValue("letterResponseWrapper");
        LetterResponseWrapper wrapper = mapper.readValue(node.toString(), LetterResponseWrapper.class);
        return wrapper;
    }

    /**
     * Helper method that extracts Response object
     *
     * @param body - json body from response
     * @param wrapperClass - response class type
     * @param jsonRoot - json root variable name
     * @return BaseResponseWrapper
     * @throws IOException
     */
    private BaseResponseWrapper extractWrapperResponse(String body, Class<? extends BaseResponseWrapper> wrapperClass, String jsonRoot)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(body).findValue(jsonRoot) != null ? mapper.readTree(body).findValue(jsonRoot) : mapper.readTree(body).findValue("baseResponseWrapper");
        BaseResponseWrapper wrapper = (BaseResponseWrapper)mapper.readValue(node.toString(), wrapperClass);
        return wrapper;
    }

    private HttpEntity<String> createHttpEntityWithAuthAndBody(String userName, String password, String body) {
        String auth = userName + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = getBaseHeaders();
        headers.set("Authorization", authHeader);
        return new HttpEntity<String>(body, headers);
    }

    private HttpHeaders getBaseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }

    private String sendRequest(String URL, HttpMethod httpMethod, String jsonBody ){

        RestTemplate emailServiceClient = null;

        try {
            emailServiceClient = NsfRestTemplate.setupRestTemplate(authenticationRequired, requestTimeout);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {

        }

        HttpEntity<String> httpEntity = null;
        if (authenticationRequired) {
            httpEntity = createHttpEntityWithAuthAndBody(emailServiceServiceUserName, emailServicePassword, jsonBody);
        } else {
            httpEntity = new HttpEntity<String>(jsonBody, getBaseHeaders());
        }

        ResponseEntity<String> response = null;
        String responseBody = null;

        try {
            response = emailServiceClient.exchange(URL, httpMethod, httpEntity, String.class);
            responseBody = response.getBody();
        } catch (HttpClientErrorException ex) {
            responseBody = ex.getResponseBodyAsString();
        } catch (HttpServerErrorException ex){
            responseBody = ex.getResponseBodyAsString();
        }

        return responseBody;
    }

    public String getEmailServiceURL() {
        return emailServiceURL;
    }

    public void setEmailServiceURL(String emailServiceURL) {
        this.emailServiceURL = emailServiceURL;
    }

    public String getEmailServiceServiceUserName() {
        return emailServiceServiceUserName;
    }

    public void setEmailServiceServiceUserName(String emailServiceServiceUserName) {
        this.emailServiceServiceUserName = emailServiceServiceUserName;
    }

    public String getEmailServicePassword() {
        return emailServicePassword;
    }

    public void setEmailServicePassword(String emailServicePassword) {
        this.emailServicePassword = emailServicePassword;
    }

    public boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public void setAuthenticationRequired(boolean authenticationRequired) {
        this.authenticationRequired = authenticationRequired;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
