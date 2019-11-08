package com.mbooking.security.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbooking.dto.UserDTO;
import com.mbooking.exception.ApiAuthException;
import com.mbooking.exception.ApiException;
import com.mbooking.model.Admin;
import com.mbooking.model.Customer;
import com.mbooking.model.User;
import com.mbooking.repository.AuthorityRepository;
import com.mbooking.repository.UserRepository;
import com.mbooking.security.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityRepository authorityRepo;

	@Override
	public UserDTO login(String email, String password) {
		UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(email, password);

		Authentication auth;
		try {
			auth = authManager.authenticate(loginToken);
		} catch (BadCredentialsException ex) {
			throw new ApiAuthException();
		}

		String token = jwtUtils.generateToken(auth.getName());
		SecurityContextHolder.getContext().setAuthentication(auth);
		UserDTO user = new UserDTO(userRepo.findByEmail(auth.getName()));
		user.setToken(token);
		return user;
	}

	@Override
	public void changePassword(String newPassword, String oldPassword) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByEmail(email);

		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
			newPassword = passwordEncoder.encode(newPassword);
			user.setPassword(newPassword);
			userRepo.save(user);
		} else {
			throw new ApiAuthException();
		}
	}

	@Override
	public UserDTO register(UserDTO userDTO) {
		if (userRepo.findByEmail(userDTO.getEmail()) == null) {
			Customer customer = new Customer();
			customer.setEmail(userDTO.getEmail());
			customer.setFirstname(userDTO.getFirstname());
			customer.setLastname(userDTO.getLastname());
			customer.setEmailConfirmed(true);
			customer.setBanned(false);
			customer.setPassword(passwordEncoder.encode(userDTO.getPassword()));
			customer.getCollectionOfAuthorities().add(authorityRepo.findByName("ROLE_CUSTOMER"));
			customer = userRepo.save(customer);
			//TODO: send confirmation email 
			
			return new UserDTO(customer);
		} else {
			throw new ApiException("Email is reserved.", HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void confirmRegistration() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = (Customer) userRepo.findByEmail(email);
		customer.setEmailConfirmed(true);
		userRepo.save(customer);
	}

	@Override
	public UserDTO createAdmin(UserDTO adminDTO) {
		if (userRepo.findByEmail(adminDTO.getEmail()) == null) {
			Admin admin = new Admin();
			admin.setEmail(adminDTO.getEmail());
			admin.setFirstname(adminDTO.getFirstname());
			admin.setLastname(adminDTO.getLastname());
			admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
			admin.getCollectionOfAuthorities().add(authorityRepo.findByName("ROLE_ADMIN"));
			admin= userRepo.save(admin);
			return new UserDTO(admin);
		} else {
			throw new ApiException("Email is reserved.", HttpStatus.BAD_REQUEST);
		}
	}
}
