package com.wesledev.libraryapi.api;

import com.wesledev.libraryapi.api.exception.ApiErrors;
import com.wesledev.libraryapi.api.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrors handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        return new ApiErrors(bindingResult);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiErrors handleBusinessException(BusinessException ex) {
        return new ApiErrors(ex);
    }

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus
    ResponseEntity handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity(new ApiErrors(ex), ex.getStatusCode());
    }
}
