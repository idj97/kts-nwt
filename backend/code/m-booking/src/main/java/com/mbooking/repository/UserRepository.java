package com.mbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	
	
}
