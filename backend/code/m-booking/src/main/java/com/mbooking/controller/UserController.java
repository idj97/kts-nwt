package com.mbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.mbooking.dto.UserDTO;
import com.mbooking.model.User;
import com.mbooking.service.UserService;
import com.mbooking.service.impl.CustomUserDetailsService;

@RestController
@RequestMapping("api")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private CustomUserDetailsService userDetailsService;
	@RequestMapping(value = "/registration", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) {
		try {
			userService.registration(null);//new User(userDTO)
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping(value = "/confirmRegistration/{id}")
	@PreAuthorize("hasRole('ADMIN', 'USER')")
	public RedirectView confirmRegistration(@PathVariable Long id) {
		User user = (User) userDetailsService.loadUserById(id);
		if (user != null) {
			user.setEnabled(true);
			userDetailsService.saveUser(user);
			return new RedirectView("http://localhost:8080/confirmedAccount.html");
		}
		return null;
	}
	
	
	
	@PutMapping(value = "/editUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> editUser(@RequestBody UserDTO userEdit) {
		  User user = (User) this.userDetailsService.loadUserByUsername(userEdit.getUsername());
		  user.setPassword(this.userDetailsService.encodePassword(userEdit.getPassword())); 
		  user.setFirstname(userEdit.getFirstname());
		  user.setLastname(userEdit.getLastname()); 
		  user.setEmail(userEdit.getEmail());
		  this.userDetailsService.saveUser(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@GetMapping(value = "/user/{userId}")
	@PreAuthorize("hasRole('ADMIN, USER')")
	public User loadById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}

	@GetMapping(value = "/user/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> loadAll() {
		return this.userService.findAll();
	}


}