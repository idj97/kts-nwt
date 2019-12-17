package com.mbooking.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbooking.dto.ChangePasswordRequestDTO;
import com.mbooking.dto.LoginRequestDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.security.impl.AuthenticationServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationServiceImpl authService;
	
	@PostMapping("/login")
	public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequest) {
		return new ResponseEntity<>(authService.login(loginRequest.getEmail(), loginRequest.getPassword()),
				HttpStatus.OK);
	}
	
	@PostMapping("/change_password")
	@Secured({"ROLE_SYS_ADMIN", "ROLE_ADMIN", "ROLE_CUSTOMER"})
	public ResponseEntity<HttpStatus> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		authService.changePassword(changePasswordRequest.getNewPassword(), changePasswordRequest.getOldPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
