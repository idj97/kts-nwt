package com.mbooking.controller;

import com.mbooking.dto.ResultsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.model.User;
import com.mbooking.security.impl.UserDetailsServiceImpl;
import com.mbooking.service.UserService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

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
	public ResponseEntity<HttpStatus> confirmRegistration(@PathVariable String email,
			@PathVariable String emailConfirmationId) {
		userService.confirmRegistration(email, emailConfirmationId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/create_admin")
	@Secured({ "ROLE_SYS_ADMIN" })
	public ResponseEntity<UserDTO> createAdmin(@RequestBody UserDTO adminDTO) {
		return new ResponseEntity<>(userService.createAdmin(adminDTO), HttpStatus.OK);
	}

	@GetMapping("/admins")
	@Secured({"ROLE_SYS_ADMIN"})
	public ResponseEntity searchAdmins(
			@RequestParam(defaultValue = "") String firstname,
			@RequestParam(defaultValue = "") String lastname,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "5") int pageSize) {
		return new ResponseEntity<>(userService.searchAdmins(firstname, lastname, email, pageNum, pageSize), HttpStatus.OK);
	}

	@GetMapping
	@Secured({"ROLE_SYS_ADMIN"})
	public ResponseEntity searchUsers(
			@RequestParam(defaultValue = "") String firstname,
			@RequestParam(defaultValue = "") String lastname,
			@RequestParam(defaultValue = "") String email,
			@RequestParam(defaultValue = "0") int pageNum,
			@RequestParam(defaultValue = "5") int pageSize) {
		return new ResponseEntity<>(userService.searchUsers(firstname, lastname, email, pageNum, pageSize), HttpStatus.OK);
	}

	@PutMapping("/ban/{id}")
	@Secured({"ROLE_SYS_ADMIN"})
	public ResponseEntity banUser(@PathVariable("id") Long id) {
		userService.banUser(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PutMapping("/unban/{id}")
	@Secured({"ROLE_SYS_ADMIN"})
	public ResponseEntity unbanUser(@PathVariable("id") Long id) {
		userService.unbanUser(id);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PutMapping
	@Secured({ "ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_SYS_ADMIN" })
	public ResponseEntity<UserDTO> editUser(@RequestBody EditProfileDTO profileDTO) {
		return new ResponseEntity<>(userService.editProfile(profileDTO), HttpStatus.OK);
	}
}