package com.mbooking.exception;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus status;
	private String code;
	
	public ApiException(String message, HttpStatus status) {
		super();
		this.message = message;
		this.status = status;
	}
	
	public ApiException(String message, HttpStatus status, String code) {
		super();
		this.message = message;
		this.status = status;
		this.code = code;
	}

	public JsonNode getValidJson() {
		ObjectNode node = new ObjectMapper().createObjectNode();
		node.put("message", this.message);
		node.put("code", this.code);
		if (this.status != null) node.put("status", this.status.toString());
		
		return node;
		
	}
	
}
