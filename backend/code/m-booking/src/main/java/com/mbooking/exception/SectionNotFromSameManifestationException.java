package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class SectionNotFromSameManifestationException extends ApiException {

	private static final long serialVersionUID = 1L;
	
	public SectionNotFromSameManifestationException() {
		super("Section not from the same manifestation", HttpStatus.BAD_REQUEST);
	}

}
