package com.mbooking.repository;

import com.mbooking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	Page<User> findByFirstnameContainingAndLastnameContainingAndEmailContaining(String firstname, String lastname, String email, Pageable pageable);
}