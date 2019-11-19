package com.mbooking.service;

import org.springframework.core.io.ByteArrayResource;

public interface EmailSenderService {
	public void sendSimpleMessage(String to, String subject, String text);
	public void sendMessageWithAttachment(String to, String subject, String message, ByteArrayResource byteResource);
}
