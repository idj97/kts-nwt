package com.mbooking.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileDTO {
	@NotEmpty(message = "Specify firstname.")
	private String firstname;
	
	@NotEmpty(message = "Specify lastname.")
	private String lastname;
}
