package com.mbooking.dto;

import java.util.List;
import java.util.stream.Collectors;


import com.mbooking.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String email;
	private String firstname;
	private String lastname;
	private String token;
	private String password;
	private String username;
	private List<String> authorities;
	
	public UserDTO(User user) {
		this.email = user.getEmail();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.password=user.getPassword();
		this.authorities = user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
	}
}
