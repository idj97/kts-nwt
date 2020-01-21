package com.mbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
		return new ResponseEntity<>(userService.register(userDTO), HttpStatus.OK);
	}

	@GetMapping("/confirm_registration/{email}/{emailConfirmationId}")
	public ResponseEntity<HttpStatus> confirmRegistration(@PathVariable String email, @PathVariable String emailConfirmationId) {
		userService.confirmRegistration(email, emailConfirmationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/create_admin")
	@Secured({"ROLE_SYS_ADMIN"})
	public ResponseEntity<UserDTO> createAdmin(@RequestBody UserDTO adminDTO) {
		return new ResponseEntity<>(userService.createAdmin(adminDTO), HttpStatus.OK);
	}

	@PutMapping("/editUser")
	@Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_SYS_ADMIN"})
	public ResponseEntity<UserDTO> editUser(@RequestBody EditProfileDTO profileDTO) {
		return new ResponseEntity<>(userService.editProfile(profileDTO), HttpStatus.OK);
	}
}