package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class SeatTakenException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public SeatTakenException() {
		super("Seat taken", HttpStatus.BAD_REQUEST);
	}

}
