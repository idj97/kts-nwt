package com.mbooking.service;

import java.util.List;

import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.model.Reservation;

public interface ReservationService {
	List<Reservation> findAllReservations();
	List<Reservation> findAllByUserEmail(String email);
	CancelReservationStatusDTO cancelReservation(Long id);
}
