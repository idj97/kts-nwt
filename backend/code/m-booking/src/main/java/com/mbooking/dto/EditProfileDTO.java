package com.mbooking.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class EditProfileDTO {
	@NotEmpty(message = "Specify email.")
	private String email;
	
	@NotEmpty(message = "Specify firstname.")
	private String firstname;
	
	@NotEmpty(message = "Specify lastname.")
	private String lastname;
}
