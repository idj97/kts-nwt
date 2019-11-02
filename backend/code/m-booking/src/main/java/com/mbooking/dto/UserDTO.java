package com.mbooking.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.mbooking.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
	private String email;
	private String firstname;
	private String lastname;
	private String token;
	private List<String> authorities;
	
	public UserDTO(User user) {
		this.email = user.getEmail();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.authorities = user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
	}
}
