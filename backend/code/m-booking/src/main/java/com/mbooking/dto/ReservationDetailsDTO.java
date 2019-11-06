package com.mbooking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationDetailsDTO {
	
	private Long manifestationSectionId;
	private boolean isSeating;
	private int row;
	private int column;
	
}
