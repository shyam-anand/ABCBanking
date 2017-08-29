package com.abcbank.banking.common;

import com.abcbank.banking.common.models.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * @author Shyam Anand (shyamwdr@gmail.com)
 *         27/08/17
 */
@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class, EntityNotFoundException.class})
    protected ResponseEntity handleNotFound(RuntimeException e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(e);
        return handleExceptionInternal(e, errorDetails, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity handleBadRequest(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, new ErrorDetails(e), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity handleGeneralException(Exception e, WebRequest request) {
        return handleExceptionInternal(e, new ErrorDetails(e), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
