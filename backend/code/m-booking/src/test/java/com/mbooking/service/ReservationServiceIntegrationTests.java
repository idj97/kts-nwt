package com.mbooking.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.mbooking.dto.MakeReservationResponseDTO;
import com.mbooking.dto.ReservationDTO;
import com.mbooking.dto.ReservationDetailsDTO;
import com.mbooking.model.Reservation;
import com.mbooking.repository.ReservationRepository;
import com.mbooking.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class ReservationServiceIntegrationTests {
	
	@Autowired
	private ReservationService  reservationService;
	
	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	ReservationDTO reservationDTO;
	
	Authentication authentication;
	
	@Before
	public void setUpAuthentication() {
		String email = "ktsnwt.customer@gmail.com";
		this.authentication = new UsernamePasswordAuthenticationToken(email, userRepository.findByEmail(email).getPassword());
		
	}
	
	@Before
	public void setUpReservationDTO() {
		this.reservationDTO = new ReservationDTO();
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDTO.setManifestationId(-1L);
		reservationDetailsDTO1.setColumn(1);
		reservationDetailsDTO1.setRow(1);
		reservationDetailsDTO1.setManifestationDayId(-1L);
		reservationDetailsDTO1.setManifestationSectionId(-1L);
		
		List<ReservationDetailsDTO> resDetails = new ArrayList<ReservationDetailsDTO>();
		resDetails.add(reservationDetailsDTO1);
		reservationDTO.setReservationDetails(resDetails);
	}
	
	@Test
	@Rollback
	@Transactional
	public void test_makeReservation_Normal() {
		List<Reservation> allReservations = reservationRepository.findAll();
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		MakeReservationResponseDTO makeDTO = reservationService.makeReservation(reservationDTO);
		
		assertEquals(4, allReservations.size() + 1);
		Optional<Reservation> reservation = reservationRepository.findById(makeDTO.getReservationId());
		assertTrue(reservation.isPresent());
		
	}
	
	@Test
	public void test_getCurrentTotalPriceForManifestationDay_Normal() {
		double expected = 0;
		double actual = reservationService.getCurrentTotalPriceForManifestationDay(-1L);
		assertEquals(expected, actual);
	}
	
	@Test
	public void test_getExpectedTotalPriceForManifestationDay_Normal() {
		double expected = 600;
		double actual = reservationService.getExpectedTotalPriceForManifestationDay(-1L);
		assertEquals(expected, actual);
	}
	
	@Test
	public void test() {
		assertEquals(1, 1);
	}
	
	
}
