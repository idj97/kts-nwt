package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class DuplicateSeatsException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateSeatsException() {
		super("", HttpStatus.BAD_REQUEST);
	}

}
