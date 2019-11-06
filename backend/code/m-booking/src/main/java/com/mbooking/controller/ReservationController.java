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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mbooking.dto.CancelReservationStatusDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.model.Reservation;
import com.mbooking.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

	@Autowired
	ReservationService resService;
	
	@GetMapping("viewall")
	public ResponseEntity<List<Reservation>> findAllReservations() {
		return new ResponseEntity<>(resService.findAllReservations(), HttpStatus.OK);
	}
	
	@PostMapping("view")
	@Secured({"ROLE_CUSTOMER"})
	public ResponseEntity<List<Reservation>> findAllReservationsFromCurrentUser() {
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
	public ResponseEntity<JsonNode> makeReservation(@RequestBody ReservationDTO reservationDTO) {
		return new ResponseEntity<>(resService.makeReservation(reservationDTO), HttpStatus.OK);
	}
	/*
	 * Test JSON for reservation
	 {
		"manifestationId" : -1,
		"manifestationDaysIds" : [-2],
		"reservationDetails" : [
			{
				"manifestationSectionId" : -1,
				"isSeating" : false,
				"row" : 1,
				"column" : 1
			},
			{
				"manifestationSectionId" : -1,
				"isSeating" : false,
				"row" : 1,
				"column" : 5
			}
		]
	 }
	 */
	
	
	
	
	@GetMapping("test")
	public ResponseEntity<String> test() {
		return new ResponseEntity<>("test", HttpStatus.OK);
	}
}
