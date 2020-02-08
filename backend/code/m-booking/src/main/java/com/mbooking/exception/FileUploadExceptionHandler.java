package com.mbooking.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class FileUploadExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JsonNode handleFileSizeException(MaxUploadSizeExceededException ex) {

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("message", "The file you uploaded is too large. Please limit size to 1MB.");
        node.put("status", "Bad request");

        return node;
    }
}
