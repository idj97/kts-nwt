package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class SectionNotFromSameManifestationException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public SectionNotFromSameManifestationException() {
		super("", HttpStatus.BAD_REQUEST);
	}

}
