package com.mbooking.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;

@Component
public class PayPalConfig {
	@Value("${paypal.client_id}")
	private String clientId;

	@Value("${paypal.client_secret}")
	private String clientSecret;

	private PayPalEnvironment env = null;
	public PayPalHttpClient client = null;

	@PostConstruct
	public void init() {
		env = new PayPalEnvironment.Sandbox(clientId, clientSecret);
		client = new PayPalHttpClient(env);
	}
	
	@Bean
	public PayPalHttpClient client() {
		return client;
	}
}
