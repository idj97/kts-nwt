package com.mbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionHandler {

    /**
     * A handler for all of the DTO validation annotations
     *  */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        String message = "message"; // used with messageNum to make message access easier
        int messageNum = 1;

        for (ObjectError err : ex.getBindingResult().getAllErrors()) {
            errors.put(message + messageNum, err.getDefaultMessage());
            messageNum++;
        }

        return errors;

    }
}
