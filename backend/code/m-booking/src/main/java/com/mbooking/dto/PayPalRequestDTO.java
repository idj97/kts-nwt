package com.mbooking.dto;

import com.paypal.orders.Order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PayPalRequestDTO {
	private String id;
	private String status;
	private String approveUrl;
	
	public PayPalRequestDTO(Order order) {
		super();
		id = order.id();
		status = order.status();
		approveUrl = order.links().get(1).href();
	}
}
