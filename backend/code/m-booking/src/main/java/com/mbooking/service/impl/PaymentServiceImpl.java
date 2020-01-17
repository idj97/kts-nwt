package com.mbooking.service.impl;

import com.braintreepayments.http.HttpResponse;
import com.braintreepayments.http.exceptions.HttpException;
import com.mbooking.dto.PayPalRequestDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationStatus;
import com.mbooking.model.User;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.UserRepository;
import com.mbooking.service.PaymentService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private ReservationRepository reservationRepo;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PayPalHttpClient payPalClient;

	@Override
	public PayPalRequestDTO makePaymentRequest(Long reservationId) {
		validateMakePayment(reservationId);
		Reservation reservation = reservationRepo.findById(reservationId).get();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.intent("CAPTURE");

		List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
		PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest();
		AmountWithBreakdown amountWithBreakdown = new AmountWithBreakdown();
		amountWithBreakdown.currencyCode("USD");
		amountWithBreakdown.value(Double.toString(reservation.getPrice()));
		purchaseUnitRequest.amount(amountWithBreakdown);
		purchaseUnits.add(purchaseUnitRequest);
		orderRequest.purchaseUnits(purchaseUnits);

		ApplicationContext applicationContext = new ApplicationContext();
		applicationContext.returnUrl("https://www.google.com");
		orderRequest.applicationContext(applicationContext);
		OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
		Order order = null;
		try {
			HttpResponse<Order> response = payPalClient.execute(request);
			order = response.result();
		} catch (IOException ex) {
			if (ex instanceof HttpException) {
				HttpException he = (HttpException) ex;
				System.out.println(he.getMessage());
				he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
			}
			throw new ApiException("Something is wrong with your payment request.", HttpStatus.BAD_REQUEST);
		}
		return new PayPalRequestDTO(order);
	}

	private void validateMakePayment(Long reservationId) {
		Optional<Reservation> opt = reservationRepo.findById(reservationId);
		if (!opt.isPresent()) {
			throw new ApiNotFoundException("Reservation not found.");
		}

		Reservation reservation = opt.get();
		User user = getCurrentUser();
		if (!reservation.getCustomer().getEmail().equals(user.getEmail())) {
			throw new ApiAuthException();
		}

		if (!reservation.getStatus().equals(ReservationStatus.CREATED)) {
			throw new ApiException("Request is expired/canceled/confirmed.", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void executePayment(String orderId, Long reservationId) {
		Optional<Reservation> optReservation = reservationRepo.findById(reservationId);
		if (optReservation.isPresent()) {
			Reservation reservation = optReservation.get();
			executeOrder(orderId);
			reservation.setStatus(ReservationStatus.CONFIRMED);
			reservationRepo.save(reservation);
		} else {
			throw new ApiNotFoundException("Reservation not found.");
		}
	}

	private Order executeOrder(String orderId) {
		OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
		Order order = null;
		try {
			HttpResponse<Order> response = payPalClient.execute(request);
			order = response.result();
		} catch (IOException ioe) {
			if (ioe instanceof HttpException) {
				HttpException he = (HttpException) ioe;
				System.out.println(he.getMessage());
				he.headers().forEach(x -> System.out.println(x + " :" + he.headers().header(x)));
			}
			throw new ApiException("Something is wrong with your payment.", HttpStatus.BAD_REQUEST);
		}
		return order;
	}

	private User getCurrentUser() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByEmail(email);
	}

}
