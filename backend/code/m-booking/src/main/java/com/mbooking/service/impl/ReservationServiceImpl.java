package com.mbooking.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.exception.ApiException;
import com.mbooking.model.Customer;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationStatus;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.UserRepository;
import com.mbooking.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService{

	@Autowired
	ReservationRepository resRep;
	
	@Autowired
	UserRepository userRep;
	
	@Override
	public List<Reservation> findAllReservations() {
		return resRep.findAll();
	}

	@Override
	public CancelReservationStatusDTO cancelReservation(Long id) {
		Optional<Reservation> optRes = resRep.findById(id);
		
		if (optRes.isPresent()) {
			Reservation reservation = optRes.get();
			
			if (reservation.getStatus() == ReservationStatus.CANCELED)
				throw new ApiException("Reservation is already canceled", HttpStatus.INTERNAL_SERVER_ERROR);
			
			reservation.setStatus(ReservationStatus.CANCELED);
			resRep.save(reservation);
			
			CancelReservationStatusDTO dto = new CancelReservationStatusDTO(
					"Canceled", "Successfully canceled reservation");
			
			return dto;
		}
		else throw new ApiException("No such reservation", HttpStatus.BAD_REQUEST);
	}

	@Override
	public List<Reservation> findAllByUserEmail(String email) {
		Customer customer = (Customer) userRep.findByEmail(email);
		if (customer != null) {
			return resRep.findAllByCustomer(customer);
		}
		else throw new ApiException("No such email", HttpStatus.BAD_REQUEST);
		
	}

	
}
