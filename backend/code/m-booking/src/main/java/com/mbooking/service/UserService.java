package com.mbooking.service;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;

public interface UserService {
	UserDTO register(UserDTO userDTO);
	void confirmRegistration(String email, String emailConfirmationId);
	UserDTO createAdmin(UserDTO adminDTO);
	UserDTO editProfile(EditProfileDTO profileDTO);
}
