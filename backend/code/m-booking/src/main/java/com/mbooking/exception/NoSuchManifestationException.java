package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class NoSuchManifestationException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public NoSuchManifestationException() {
		super("No such manifestation", HttpStatus.BAD_REQUEST);
	}

}
