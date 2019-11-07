package com.mbooking.dto;

public class MessageDTO {
	private String message;
	private String header;

	public MessageDTO(String message, String header) {
		super();
		this.message = message;
		this.setHeader(header);
	}

	public MessageDTO() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
}
