package com.mbooking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mbooking.model.User;
import com.mbooking.repository.UserRepository;
import com.mbooking.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	

	@Override
	public User registration(User user) throws Exception {
		
		return null;
	}

	@Override
	public String editProfile(User user) {
		User userToEdit = userRepository.findOneByUsername(user.getUsername());
		if (userToEdit == null) {
			return "User with given username does not exist.";
		}

		String firstName = user.getFirstname();
		if (firstName != null) {
			userToEdit.setFirstname(firstName);
		}

		String lastName = user.getLastname();
		if (lastName != null) {
			userToEdit.setLastname(lastName);
		}

		try {
			userRepository.save(userToEdit);
		} catch (Exception e) {
			return "Database error.";
		}

		return null;
	}

	@Override
	public User findByUsername(String username) {
		
		return userRepository.findByUsername(username);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
		
	}
	
}
