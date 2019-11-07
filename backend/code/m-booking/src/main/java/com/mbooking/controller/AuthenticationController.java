package com.mbooking.controller;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbooking.dto.ChangePasswordRequestDTO;
import com.mbooking.dto.LoginRequestDTO;
import com.mbooking.dto.MessageDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.model.Admin;
import com.mbooking.model.Authority;
import com.mbooking.model.RegularUser;
import com.mbooking.model.Reservation;
import com.mbooking.model.User;

import com.mbooking.security.impl.AuthenticationServiceImpl;
import com.mbooking.service.impl.CustomUserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationServiceImpl authService;

	
	@PostMapping(value = "/login")
	public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequest) {
		return new ResponseEntity<>(authService.login(loginRequest.getEmail(), loginRequest.getPassword()), HttpStatus.OK);
	}

	@PostMapping(value = "/change_password")
	@Secured({ "SYS_ADMIN", "ADMIN",  })
	public ResponseEntity<HttpStatus> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		authService.changePassword(changePasswordRequest.getNewPassword(), changePasswordRequest.getOldPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	

	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@PostMapping(value = "/registerUser")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO user) {
		
		
		if (this.userDetailsService.usernameTaken(user.getUsername())) {
			return new ResponseEntity<>(new MessageDTO("Username is already take.", "Error"), HttpStatus.OK);
		}
		User regularUser = new RegularUser();
		regularUser.setId(user.getId());
		regularUser.setEmail(user.getEmail());
		regularUser.setUsername(user.getUsername());
		regularUser.setPassword(this.userDetailsService.encodePassword(user.getPassword()));
		List<Authority> authorities = new ArrayList<>();
		Authority a = new Authority();
		//a.setName(UserRole.USER);
		authorities.add(a);
		//regularUser.setAuthorities(user.getAuthorities());
		regularUser.setEnabled(true);
		regularUser.setFirstname(user.getFirstname());
		regularUser.setLastname(user.getLastname());
		regularUser.setPassword(user.getPassword());
		
		if (this.userDetailsService.saveUser(regularUser)) { 
			return new ResponseEntity<>(true, HttpStatus.OK); 
			}
		 
		return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
	
	}
	@PostMapping(value = "auth/registerAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registerAdmin(@RequestBody UserDTO user) {
		
		if (this.userDetailsService.usernameTaken(user.getUsername())) {
			return new ResponseEntity<>(new MessageDTO("Username is already take.", "Error"), HttpStatus.OK);
		}
		Admin admin = new Admin();
		admin.setId(user.getId());
		admin.setEmail(user.getEmail());
		admin.setUsername(user.getUsername());
		admin.setPassword(this.userDetailsService.encodePassword(user.getPassword()));
		List<Authority> authorities = new ArrayList<>();
		Authority a = new Authority();
		//a.setName(UserRoleName.ROLE_ADMIN);
		authorities.add(a);
		//admin.setAuthorities(authorities);
		admin.setEnabled(true);
		admin.setFirstname(user.getFirstname());
		admin.setLastname(user.getLastname());
		admin.setPassword(user.getPassword());
		
		if (this.userDetailsService.saveUser(admin)) { 
			return new ResponseEntity<>(true, HttpStatus.OK); 
			}
		 
		return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
	}
	
	
	
}
