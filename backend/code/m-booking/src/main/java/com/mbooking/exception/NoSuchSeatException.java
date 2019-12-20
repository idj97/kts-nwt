package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchSeatException extends ApiException{

	private static final long serialVersionUID = 1L;
	
	public NoSuchSeatException() {
		super("No such seat", HttpStatus.BAD_REQUEST);
	}

	
}
