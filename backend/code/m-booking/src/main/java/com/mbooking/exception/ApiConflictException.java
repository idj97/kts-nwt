package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ApiConflictException extends ApiException {

	private static final long serialVersionUID = 1L;

	public ApiConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

}
