package com.mbooking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mbooking.model.User;
import com.mbooking.repository.UserRepository;
@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Funkcija koja na osnovu username-a iz baze vraca objekat User-a
	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findOneByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;
		}
	}

	public boolean saveUser(User us) {
		this.userRepository.save(us);
		/*
		 * try { this.userRepository.save(us); } catch (Exception e) { return false; }
		 */
		return true;
	}

	public UserDetails loadUserById(long id) {
		User user = userRepository.findById(id);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with id '%s'.", id));
		} else {
			return user;
		}
	}

	public String encodePassword(String password) {

		return this.passwordEncoder.encode(password);
	}

	
	public boolean usernameTaken(String username) {
		User user = userRepository.findOneByUsername(username);

		return user != null;
	}
}
