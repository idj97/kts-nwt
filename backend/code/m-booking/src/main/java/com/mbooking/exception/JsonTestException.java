package com.mbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK, reason = "Everything is ok!")
public class JsonTestException extends Exception {
    public JsonTestException(String message) {
        super(message);
    }
}
