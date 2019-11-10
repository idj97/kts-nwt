package com.mbooking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;
import com.mbooking.model.User;
import com.mbooking.repository.UserRepository;
import com.mbooking.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDTO editProfile(EditProfileDTO profileDTO) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByEmail(email);
		user.setFirstname(profileDTO.getFirstname());
		user.setLastname(profileDTO.getLastname());
		user = userRepo.save(user);
		
		//TODO: maybe allow change email?
		
		return new UserDTO(user);
	}
}
