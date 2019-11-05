package com.mbooking.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.Customer;
import com.mbooking.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {
	List<Reservation> findAll();
	List<Reservation> findAllByCustomer(Customer customer);
	List<Reservation> findByExpirationDateBefore(Date date);
}
