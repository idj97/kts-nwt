package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchReservationException extends ApiException {

private static final long serialVersionUID = 1L;
	
	public NoSuchReservationException() {
		super("No such reservation", HttpStatus.BAD_REQUEST);
	}
	
}
