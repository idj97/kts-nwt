package com.mbooking.exception;

import org.springframework.http.HttpStatus;

public class ApiAuthException extends ApiException {
	private static final long serialVersionUID = 1L;

	public ApiAuthException() {
		super("Bad credentials", HttpStatus.UNAUTHORIZED);
	}

	public ApiAuthException(String message) {
		super(message, HttpStatus.UNAUTHORIZED);
	}
}
