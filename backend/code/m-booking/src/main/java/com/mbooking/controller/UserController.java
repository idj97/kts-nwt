package com.mbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PutMapping
	@Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_SYS_ADMIN"})
	public ResponseEntity<UserDTO> editUser(@RequestBody EditProfileDTO profileDTO) {
		return new ResponseEntity<>(userService.editProfile(profileDTO), HttpStatus.OK);
	}
}