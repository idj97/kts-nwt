package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ReservationCannotBeCanceledException extends ApiException {
	
	private static final long serialVersionUID = 1L;
	
	public ReservationCannotBeCanceledException() {
		super("Reservation cannot be canceled", HttpStatus.BAD_REQUEST);
	}

}
