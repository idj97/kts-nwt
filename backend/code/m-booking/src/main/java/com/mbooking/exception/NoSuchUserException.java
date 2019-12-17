package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchUserException extends ApiException{

	private static final long serialVersionUID = 1L;
	
	public NoSuchUserException() {
		super("", HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
