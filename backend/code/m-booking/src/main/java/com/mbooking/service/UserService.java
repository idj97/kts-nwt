package com.mbooking.service;

import com.mbooking.model.User;

public interface UserService {
	User registration(User user) throws Exception;

	User findByUsername(String username);

	String editProfile(User user);

	void save(User user);

}
