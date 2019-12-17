package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchSectionException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchSectionException() {
		super("", HttpStatus.BAD_REQUEST);
	}

}
