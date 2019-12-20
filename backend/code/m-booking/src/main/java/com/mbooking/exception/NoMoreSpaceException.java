package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoMoreSpaceException extends ApiException {

	private static final long serialVersionUID = 1L;

	public NoMoreSpaceException() {
		super("No more space", HttpStatus.BAD_REQUEST);
	}
}
