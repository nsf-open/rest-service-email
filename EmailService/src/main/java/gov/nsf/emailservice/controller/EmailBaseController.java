package gov.nsf.emailservice.controller;


import gov.nsf.common.ember.model.EmberModel;
import gov.nsf.common.exception.FormValidationException;
import gov.nsf.common.exception.ResourceNotFoundException;
import gov.nsf.common.exception.RollbackException;
import gov.nsf.common.model.BaseError;
import gov.nsf.common.model.BaseResponseWrapper;
import gov.nsf.emailservice.api.model.InvalidSearchParameterException;
import gov.nsf.emailservice.api.model.LetterAlreadySentException;
import gov.nsf.emailservice.common.util.Constants;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import gov.nsf.emailservice.common.util.Constants;

import java.util.Collections;

/**
 *         Exception/Error handling mechanism to all sub class Controllers
 */

@RestController
public class EmailBaseController {


    private static final Logger LOGGER = Logger.getLogger(EmailBaseController.class);
    /**
     * Response handler for LetterAlreadySentException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({ LetterAlreadySentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EmberModel processLetterAlreadySentException(LetterAlreadySentException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.setErrors(Collections.singletonList(new BaseError(Constants.ELTR_STATUS_FIELD, ex.getErrMsg())));
        LOGGER.error("EmailBaseController - Letter Already Sent : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for FormValidationException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({ InvalidSearchParameterException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EmberModel processInvalidSearchParameterException(InvalidSearchParameterException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.setErrors(Collections.singletonList(new BaseError(Constants.SEARCH_PARAMETER_KEY_FIELD, ex.getErrMsg())));
        LOGGER.error("EmailBaseController - Invalid Search Parameters : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for HttpMessageNotReadableException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EmberModel processFormValidationException(HttpMessageNotReadableException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.UNABLE_TO_READ_JSON, ex.getMessage() ));
        LOGGER.error("EmailBaseController - Bad Request : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for FormValidationException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({ FormValidationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EmberModel processFormValidationException(FormValidationException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.setErrors(ex.getValidationErrors());
        LOGGER.error("EmailBaseController - Bad Request : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for MissingServletRequestParameterException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public EmberModel processFormValidationException(MissingServletRequestParameterException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.INVALID_REQUEST_PARAMETER, ex.getMessage()));
        LOGGER.error("EmailBaseController - Invalid Request Parameters : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for ResourceNotFoundException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public EmberModel processResourceNotFoundException(ResourceNotFoundException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.ELTR_ID_FIELD, ex.getErrMsg()));
        LOGGER.error("EmailBaseController - Eltr ID Field is missing : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for RollbackException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({RollbackException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public EmberModel processResourceNotFoundException(RollbackException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.DB_TRANSACTION_ERROR, ex.getErrMsg()));
        LOGGER.error("EmailBaseController - DB Transaction Error : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }

    /**
     * Response handler for AccessDeniedException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public EmberModel processException(AccessDeniedException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.ACCESS_DENIED_EXCEPTION, ex.getMessage()));
        LOGGER.error("EmailBaseController - Access Denied Exception : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();

    }

    /**
     * Response handler for Exception exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Letter field
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public EmberModel processException(Exception ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.SERVER_500_ERROR, ExceptionUtils.getFullStackTrace(ex)));
        LOGGER.error("EmailBaseController - Server Error : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }
    /**
     * Response handler for MailException exceptions
     * @param ex
     * @return BaseResponseWrapper w/ empty Email field
     */
    @ExceptionHandler({MailException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public EmberModel sendEmailException(MailException ex) {
        BaseResponseWrapper response = new BaseResponseWrapper();
        response.addError(new BaseError(Constants.SERVER_500_ERROR, ex.getMessage()));
        LOGGER.error("EmailBaseController - Mail Exception : " + ex);
        return new EmberModel.Builder<BaseResponseWrapper>(Constants.BASE_RESPONSE_WRAPPER, response).build();
    }
}
