package com.mbooking.service;

import com.mbooking.dto.EditProfileDTO;
import com.mbooking.dto.UserDTO;

public interface UserService {
	UserDTO editProfile(EditProfileDTO profileDTO);
}
