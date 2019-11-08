package com.mbooking.security;

import com.mbooking.dto.UserDTO;

public interface AuthenticationService {
	UserDTO login(String email, String password);
	void changePassword(String newPassword, String oldPassword);
	UserDTO register(UserDTO userDTO);
	void confirmRegistration();
	UserDTO createAdmin(UserDTO adminDTO);
}
