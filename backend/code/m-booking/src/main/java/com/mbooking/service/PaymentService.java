package com.mbooking.service;

import com.mbooking.dto.PayPalRequestDTO;

public interface PaymentService {
	PayPalRequestDTO makePaymentRequest(Long reservationId);
	void executePayment(String orderId, Long reservationId);
}
