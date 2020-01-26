package com.mbooking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReservationDetailsRequestDTO {
	private Long manifestationDayId;
	private Long manifestationId;
}
