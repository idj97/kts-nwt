package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class MaxReservationsException extends ApiException {

	private static final long serialVersionUID = 1L;

	public MaxReservationsException() {
		super("Max reservation limit reached", HttpStatus.BAD_REQUEST);
	}
	
}
