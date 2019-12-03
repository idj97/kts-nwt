package com.mbooking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MakeReservationResponseDTO {

	private String message;
	private String manifestation;
	private Long manifestationId;
	private String expirationDate;
	private Long reservationId;
	
}
