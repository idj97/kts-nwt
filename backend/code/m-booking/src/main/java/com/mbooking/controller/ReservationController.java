package com.mbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.MakeReservationResponseDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.dto.ReservationDetailsRequestDTO;
import com.mbooking.dto.ViewReservationDTO;
import com.mbooking.service.PaymentService;
import com.mbooking.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

	@Autowired
	ReservationService resService;
	
	@Autowired
	PaymentService paymentService;
	
	@GetMapping("day_expected_total_price/{id}")		//Test Manifestation day id
	public ResponseEntity<Double> getExpectedTotalPriceForManifestationDay(@PathVariable("id") Long id) {
		return new ResponseEntity<>(resService.getExpectedTotalPriceForManifestationDay(id), HttpStatus.OK);
	}
	
	@GetMapping("expected_total_price/{id}")	//Test Manifestation id
	public ResponseEntity<Double> getExpectedTotalPriceForManifestation(@PathVariable("id") Long id) {
		return new ResponseEntity<>(resService.getExpectedTotalPriceForManifestation(id), HttpStatus.OK);
	}
	
	@GetMapping("viewall")
	public ResponseEntity<List<ViewReservationDTO>> findAllReservations() {
		return new ResponseEntity<>(resService.findAllReservations(), HttpStatus.OK);
	}
	
	@PostMapping("view")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<List<ViewReservationDTO>> findAllReservationsFromCurrentUser() {
		String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
		return new ResponseEntity<>(resService.findAllByUserEmail(currentPrincipalName), HttpStatus.OK);
	}
	
	@PostMapping("cancel/{id}")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<CancelReservationStatusDTO> cancelReservation(@PathVariable("id") Long id) {
		return new ResponseEntity<>(resService.cancelReservation(id), HttpStatus.OK);
	}
	
	@PostMapping("reserve")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<MakeReservationResponseDTO> makeReservation(@RequestBody ReservationDTO reservationDTO) {
		return new ResponseEntity<>(resService.makeReservation(reservationDTO), HttpStatus.OK);
	}
	
	@PostMapping("viewCurrentUserManifestationDetails")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<List<ReservationDetailsDTO>> viewCurrentUserManifestationDetails(@RequestBody ReservationDetailsRequestDTO rdr) {
		return new ResponseEntity<>(resService.getTotalCustomerReservationDetailsByManifestationAndManifestationDay(rdr), HttpStatus.OK);
	}
	
	@PostMapping("viewAllManifestationDetails")
	public ResponseEntity<List<ReservationDetailsDTO>> viewAllManifestationDetails(@RequestBody ReservationDetailsRequestDTO rdr) {
		return new ResponseEntity<>(resService.getAllReservationsDetailsByManifestationAndManifestationDay(rdr), HttpStatus.OK);
	}
	
}
