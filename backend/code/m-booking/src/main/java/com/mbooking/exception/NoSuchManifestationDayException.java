package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchManifestationDayException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchManifestationDayException() {
		super("", HttpStatus.BAD_REQUEST);
	}

	
}
