package com.mbooking.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import com.mbooking.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
	private Long id;
	
	@NotEmpty(message = "Provide email.")
	private String email;
	
	@NotEmpty(message = "Provide firstname.")
	private String firstname;
	
	@NotEmpty(message = "Provide lastname.")
	private String lastname;
	
	@NotEmpty(message = "Provide password.")
	private String password;
	
	private String token;
	private List<String> authorities;
	private Boolean banned;

	public UserDTO(User user) {
		this.email = user.getEmail();
		this.firstname = user.getFirstname();
		this.lastname = user.getLastname();
		this.authorities = user.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.toList());
		this.id = user.getId();
		this.banned = user.isBanned();
	}

	public UserDTO(String email, String firstna, String lastname, String password, String token, List<String> authorities) {
		super();
		this.email = email;
		this.firstname = firstname;
		this.lastname = lastname;
		this.password = password;
		this.token = token;
		this.authorities = authorities;
	}
}
