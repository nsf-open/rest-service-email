package gov.nsf.emailservice.controller;

import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseResponseWrapper;
import gov.nsf.emailservice.api.model.*;
import gov.nsf.emailservice.api.model.request.SendLetterRequest;
import gov.nsf.emailservice.common.util.Utils;
import gov.nsf.emailservice.validator.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import gov.nsf.common.ember.model.EmberModel;
import gov.nsf.emailservice.common.util.Constants;
import gov.nsf.emailservice.api.service.EmailService;

import java.util.Map;

/**
 * Controller to serve /emailservice requests
 *
 */
@RestController
public class EmailController extends EmailBaseController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailServiceValidatorFactory emailServiceValidatorFactory;


    /**
     * GET handler for /letter/{id}
     *
     * @return JSON response of Letter object
     */
    @RequestMapping(value = {"/letter/{id}", "/auth/letter/{id}"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Get Letter",
            notes = "This API returns Letter.",
            response = LetterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel getLetter(@PathVariable String id) throws RollbackException, FormValidationException {
        emailServiceValidatorFactory.getValidator("getLetter").validateRequest(id);
        LetterResponseWrapper wrapper = emailService.getLetter(id);
        return new EmberModel.Builder<LetterResponseWrapper>(Constants.LETTER_RESPONSE_WRAPPER, wrapper).build();
    }

    /**
     * GET handler for /letter/{id}/parameters
     *
     * @return JSON response of search parameter list
     */
    @RequestMapping(value = {"/letter/{id}/searchparameters", "/auth/letter/{id}/searchparameters"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Get Search Parameters for Letter",
            notes = "This API returns Search Parameters for Letter.",
            response = SearchParameterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel getSearchParametersForLetter(@PathVariable String id) throws RollbackException, FormValidationException {
        emailServiceValidatorFactory.getValidator("getSearchParameters").validateRequest(id);
        SearchParameterResponseWrapper wrapper = emailService.getSearchParameters(id);
        return new EmberModel.Builder<SearchParameterResponseWrapper>(Constants.SEARCH_PARAMETER_RESPONSE_WRAPPER, wrapper).build();
    }

    /**
     * GET handler for /letter?paramName=paramVal
     *
     * @return JSON response of Letter object
     */
    @RequestMapping(value = {"/letter", "/auth/letter"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Find Letter",
            notes = "This API returns Letter with the search paramters.",
            response = SearchParameterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel findLetter(@RequestParam Map<String, String> parameterMap) throws RollbackException, FormValidationException {
        Letter letter = new Letter();
        letter.setSearchParameters(Utils.getSearchParametersFromMap(parameterMap));
        emailServiceValidatorFactory.getValidator("findLetter").validateRequest(letter);

        SearchParameter searchParameter = letter.getSearchParameters().get(0);
        LetterListResponseWrapper wrapper = emailService.findLetter(searchParameter);
        return new EmberModel.Builder<LetterListResponseWrapper>("letterListResponseWrapper", wrapper).build();
    }

    /**
     * POST handler for /letter
     *
     * @return JSON response of Letter object
     */
    @RequestMapping(value = {"/letter", "/auth/letter"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Create Letter",
            notes = "This API Create Letter with the search parameters provided.",
            response = SearchParameterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel createLetter(@RequestBody Letter letter, @RequestParam(value = "templateID", required = false) String templateID) throws RollbackException, FormValidationException {

        letter.setTmplID(templateID);
        emailServiceValidatorFactory.getValidator("createLetter").validateRequest(letter);
        LetterResponseWrapper wrapper = emailService.saveLetter(letter, templateID);
        return new EmberModel.Builder<LetterResponseWrapper>(Constants.LETTER_RESPONSE_WRAPPER, wrapper).build();
    }

    /**
     * PUT handler for /letter/{id}
     *
     * @return JSON response of Letter object
     */
    @RequestMapping(value = {"/letter/{id}", "/auth/letter/{id}"}, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Update Letter",
            notes = "This API Update Letter with the information provided in Letter object.",
            response = SearchParameterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel updateLetter(@RequestBody Letter letter, @PathVariable String id) throws RollbackException, FormValidationException {
        letter.setEltrID(id);
        emailServiceValidatorFactory.getValidator("updateLetter").validateRequest(letter);
        LetterResponseWrapper wrapper = emailService.updateLetter(letter);
        return new EmberModel.Builder<LetterResponseWrapper>(Constants.LETTER_RESPONSE_WRAPPER, wrapper).build();
    }

    /**
     * PUT handler for /letter/{id}
     *
     * @return JSON response of Letter object
     */
    @RequestMapping(value = {"/letter/{id}", "/auth/letter/{id}"}, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Delete Letter",
            notes = "This API Delete Letter.",
            response = SearchParameterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel deleteLetter(@PathVariable String id) throws RollbackException, FormValidationException {
        emailServiceValidatorFactory.getValidator("deleteLetter").validateRequest(id);
        LetterResponseWrapper wrapper = emailService.deleteLetter(id);
        return new EmberModel.Builder<LetterResponseWrapper>(Constants.LETTER_RESPONSE_WRAPPER, wrapper).build();
    }

    /**
     * POST handler for /sendletter
     *
     * @return JSON response of email object
     */
    @RequestMapping(value = {"/sendletter","/auth/sendletter"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "Send Letter",
            notes = "This API Send Letter.",
            response = SearchParameterResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Input/Business Validation Error"),
            @ApiResponse(code = 404, message = "Not Found")})
    public EmberModel sendLetter(@RequestBody SendLetterRequest letterRequest) throws FormValidationException {
        new SendLetterValidator().validateRequest(letterRequest);
        BaseResponseWrapper wrapper = emailService.sendLetter(letterRequest.getLetter(), letterRequest.getSendMetaData());
        return new EmberModel.Builder<BaseResponseWrapper>("baseResponseWrapper", wrapper).build();
    }


}
