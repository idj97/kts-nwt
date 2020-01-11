package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchSectionException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchSectionException() {
		super("No such section", HttpStatus.BAD_REQUEST);
	}

}
