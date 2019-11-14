package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ApiInternalServerErrorException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	
	public ApiInternalServerErrorException(String message) {
		super(message, HttpStatus.BAD_REQUEST);
	}
	
	public ApiInternalServerErrorException() {
		super("", HttpStatus.BAD_REQUEST);
	}

}
