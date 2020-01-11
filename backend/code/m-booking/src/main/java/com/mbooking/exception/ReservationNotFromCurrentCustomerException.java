package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ReservationNotFromCurrentCustomerException extends ApiException {

private static final long serialVersionUID = 1L;
	
	public ReservationNotFromCurrentCustomerException() {
		super("Reservation not from current customer", HttpStatus.BAD_REQUEST);
	}
	
}
