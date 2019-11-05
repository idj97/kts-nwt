package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ApiNotFoundException extends ApiException {

	private static final long serialVersionUID = 1L;

	public ApiNotFoundException(String message) {
		super(message, HttpStatus.NOT_FOUND);
	}
	
	public ApiNotFoundException() {
		super("", HttpStatus.NOT_FOUND);
	}
	
}
