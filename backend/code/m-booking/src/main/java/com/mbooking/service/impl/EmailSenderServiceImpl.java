package com.mbooking.service.impl;

import com.mbooking.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
	
	@Autowired
    public JavaMailSender emailSender;
	
	//Might be useful for testing
	public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
    }
	
	public void sendMessageWithAttachment(String to,
			String subject,
			String message,
			String attachmentName,
			ByteArrayResource byteResource) {
		
		MimeMessage mimeMessage = emailSender.createMimeMessage();
        
        try {
        	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message);
	        helper.addAttachment(attachmentName, byteResource);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
        
		emailSender.send(mimeMessage);
	}
	
	
}
