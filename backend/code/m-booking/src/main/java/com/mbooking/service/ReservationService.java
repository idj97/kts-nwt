package com.mbooking.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ViewReservationDTO;

public interface ReservationService {
	List<ViewReservationDTO> findAllReservations();
	List<ViewReservationDTO> findAllByUserEmail(String email);
	CancelReservationStatusDTO cancelReservation(Long id);
	JsonNode makeReservation(ReservationDTO dto);
	double getExpectedTotalPriceForManifestation(Long id);
	double getCurrentTotalPriceForManifestation(Long id);
	double getExpectedTotalPriceForManifestationDay(Long id);
	double getCurrentTotalPriceForManifestationDay(Long id);
}
