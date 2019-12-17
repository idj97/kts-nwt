package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ReservableUntilException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public ReservableUntilException() {
		super("", HttpStatus.BAD_REQUEST);
	}

}
