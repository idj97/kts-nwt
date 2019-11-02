package com.mbooking.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus status;
}
