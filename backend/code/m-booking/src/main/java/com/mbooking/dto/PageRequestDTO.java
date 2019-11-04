package com.mbooking.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageRequestDTO {
	
	@NotNull(message = "Specify page number")
	@PositiveOrZero(message = "Page number must be non-negative")
	private int page;
	
	@NotNull(message = "Specify page size")
	@Positive(message = "Page size must be positive")
	private int pageSize;
}
