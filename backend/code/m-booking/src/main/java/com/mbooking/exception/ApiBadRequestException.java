package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ApiBadRequestException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public ApiBadRequestException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
	
	public ApiBadRequestException() {
		super("", HttpStatus.BAD_REQUEST);
	}

}
