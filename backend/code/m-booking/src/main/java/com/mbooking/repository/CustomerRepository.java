package com.mbooking.repository;

import com.mbooking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Customer findByEmail(String email);
}
