package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ManifestationReservationsAvailableException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public ManifestationReservationsAvailableException() {
		super("Manifestation is not available", HttpStatus.BAD_REQUEST);
	}
	
}
