package com.mbooking.service;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.ResultsDTO;
import com.mbooking.dto.UserDTO;

import java.util.List;

public interface UserService {
	UserDTO register(UserDTO userDTO);
	void confirmRegistration(String email, String emailConfirmationId);
	UserDTO createAdmin(UserDTO adminDTO);
	UserDTO editProfile(EditProfileDTO profileDTO);
	ResultsDTO<UserDTO> searchAdmins(String firstname, String lastname, String email, int pageNum, int pageSize);
	ResultsDTO<UserDTO> searchUsers(String firstname, String lastname, String email, int pageNum, int pageSize);
	void banUser(Long id);
}
