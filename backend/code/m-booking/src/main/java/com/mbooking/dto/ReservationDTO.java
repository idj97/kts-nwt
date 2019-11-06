package com.mbooking.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDTO {
	
	private Long manifestationId;
	private List<Long> manifestationDaysIds;
	//private List<Date> reservationDates;
	private List<ReservationDetailsDTO> reservationDetails;
	
	
}
