package com.mbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbooking.dto.PayPalRequestDTO;
import com.mbooking.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/{reservationId}/request_payment")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<PayPalRequestDTO> requestPayment(@PathVariable("reservationId") Long reservationId) {
		return new ResponseEntity<>(paymentService.makePaymentRequest(reservationId), HttpStatus.OK);
	}

	@PostMapping("/{reservationId}/{orderId}/execute_payment")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<HttpStatus> executePayment(@PathVariable("reservationId") Long reservationId, @PathVariable("orderId") String orderId) {
		paymentService.executePayment(orderId, reservationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
