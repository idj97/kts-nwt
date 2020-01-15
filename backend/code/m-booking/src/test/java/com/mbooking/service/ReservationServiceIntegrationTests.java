package com.mbooking.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
import com.mbooking.exception.DuplicateSeatsException;
import com.mbooking.exception.EmptyReservationDetailsException;
import com.mbooking.exception.ManifestationReservationsAvailableException;
import com.mbooking.exception.MaxReservationsException;
import com.mbooking.exception.NoMoreSpaceException;
import com.mbooking.exception.NoSuchManifestationDayException;
import com.mbooking.exception.NoSuchManifestationException;
import com.mbooking.exception.NoSuchSeatException;
import com.mbooking.exception.NoSuchSectionException;
import com.mbooking.exception.NoSuchUserException;
import com.mbooking.exception.ReservableUntilException;
import com.mbooking.exception.SeatTakenException;
import com.mbooking.exception.SectionNotFromSameManifestationException;
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
	
	/* 
	 * TESTS
	 * 
	 * */
	
	@Test(expected = NoSuchUserException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoSuchUser() {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("dummytest@gmail.com", "dummytest"));
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = NoMoreSpaceException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoMoreSpace() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setReservationDetails(new ArrayList<ReservationDetailsDTO>());
		reservationDTO.setManifestationId(-2L);
		for (int i = 0; i < 2; i++) {
			ReservationDetailsDTO resdto = new ReservationDetailsDTO();
			resdto.setColumn(0);
			resdto.setRow(0);
			resdto.setManifestationDayId(-3L);
			resdto.setManifestationSectionId(-4L);
			reservationDTO.getReservationDetails().add(resdto);
		}
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = SeatTakenException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_SeatTaken() {
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = NoSuchSeatException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoSuchSeat_Negative() {
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDetailsDTO1.setColumn(-34);
		reservationDetailsDTO1.setRow(-34);
		reservationDetailsDTO1.setManifestationDayId(-1L);
		reservationDetailsDTO1.setManifestationSectionId(-1L);
		this.reservationDTO.getReservationDetails().add(reservationDetailsDTO1);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = NoSuchSeatException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoSuchSeat_Positive() {
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDetailsDTO1.setColumn(34);
		reservationDetailsDTO1.setRow(34);
		reservationDetailsDTO1.setManifestationDayId(-1L);
		reservationDetailsDTO1.setManifestationSectionId(-1L);
		this.reservationDTO.getReservationDetails().add(reservationDetailsDTO1);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = NoSuchManifestationDayException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoSuchManifestationDay() {
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDetailsDTO1.setColumn(1);
		reservationDetailsDTO1.setRow(1);
		reservationDetailsDTO1.setManifestationDayId(-3L);
		reservationDetailsDTO1.setManifestationSectionId(-1L);
		this.reservationDTO.getReservationDetails().add(reservationDetailsDTO1);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = MaxReservationsException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_MaxReservations_Total() {
		this.reservationDTO.setManifestationId(-1L);
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDetailsDTO1.setColumn(1);
		reservationDetailsDTO1.setRow(1);
		reservationDetailsDTO1.setManifestationDayId(-1L);
		reservationDetailsDTO1.setManifestationSectionId(-1L);
		ReservationDetailsDTO reservationDetailsDTO2 = new ReservationDetailsDTO();
		reservationDetailsDTO2.setColumn(2);
		reservationDetailsDTO2.setRow(1);
		reservationDetailsDTO2.setManifestationDayId(-1L);
		reservationDetailsDTO2.setManifestationSectionId(-1L);
		ReservationDetailsDTO reservationDetailsDTO3 = new ReservationDetailsDTO();
		reservationDetailsDTO3.setColumn(3);
		reservationDetailsDTO3.setRow(1);
		reservationDetailsDTO3.setManifestationDayId(-1L);
		reservationDetailsDTO3.setManifestationSectionId(-1L);
		List<ReservationDetailsDTO> resDetails = new ArrayList<ReservationDetailsDTO>();
		resDetails.add(reservationDetailsDTO1);
		resDetails.add(reservationDetailsDTO2);
		resDetails.add(reservationDetailsDTO3);
		reservationDTO.setReservationDetails(resDetails);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = MaxReservationsException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_MaxReservations() {
		this.reservationDTO.setManifestationId(-2L);
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDetailsDTO1.setColumn(1);
		reservationDetailsDTO1.setRow(1);
		reservationDetailsDTO1.setManifestationDayId(-3L);
		reservationDetailsDTO1.setManifestationSectionId(-3L);
		ReservationDetailsDTO reservationDetailsDTO2 = new ReservationDetailsDTO();
		reservationDetailsDTO2.setColumn(2);
		reservationDetailsDTO2.setRow(1);
		reservationDetailsDTO2.setManifestationDayId(-3L);
		reservationDetailsDTO2.setManifestationSectionId(-3L);
		ReservationDetailsDTO reservationDetailsDTO3 = new ReservationDetailsDTO();
		reservationDetailsDTO3.setColumn(3);
		reservationDetailsDTO3.setRow(1);
		reservationDetailsDTO3.setManifestationDayId(-3L);
		reservationDetailsDTO3.setManifestationSectionId(-3L);
		List<ReservationDetailsDTO> resDetails = new ArrayList<ReservationDetailsDTO>();
		resDetails.add(reservationDetailsDTO1);
		resDetails.add(reservationDetailsDTO2);
		resDetails.add(reservationDetailsDTO3);
		reservationDTO.setReservationDetails(resDetails);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = ReservableUntilException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_ReservableUntil() {
		ReservationDTO reservationDTO = new ReservationDTO();
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDTO.setManifestationId(-4L);
		reservationDetailsDTO1.setColumn(1);
		reservationDetailsDTO1.setRow(1);
		reservationDetailsDTO1.setManifestationDayId(-7L);
		reservationDetailsDTO1.setManifestationSectionId(-7L);
		List<ReservationDetailsDTO> resDetails = new ArrayList<ReservationDetailsDTO>();
		resDetails.add(reservationDetailsDTO1);
		reservationDTO.setReservationDetails(resDetails);
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = ManifestationReservationsAvailableException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_ReservationsNotAvailable() {
		
		ReservationDTO reservationDTO = new ReservationDTO();
		ReservationDetailsDTO reservationDetailsDTO1 = new ReservationDetailsDTO();
		reservationDTO.setManifestationId(-3L);
		reservationDetailsDTO1.setColumn(1);
		reservationDetailsDTO1.setRow(1);
		reservationDetailsDTO1.setManifestationDayId(-5L);
		reservationDetailsDTO1.setManifestationSectionId(-5L);
		List<ReservationDetailsDTO> resDetails = new ArrayList<ReservationDetailsDTO>();
		resDetails.add(reservationDetailsDTO1);
		reservationDTO.setReservationDetails(resDetails);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = SectionNotFromSameManifestationException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_SectionNotFromSameManifestation() {
		ReservationDetailsDTO resdto = new ReservationDetailsDTO();
		resdto.setColumn(1);
		resdto.setRow(1);
		resdto.setManifestationDayId(-1L);
		resdto.setManifestationSectionId(-3L);
		
		reservationDTO.getReservationDetails().add(resdto);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = DuplicateSeatsException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_DuplicateSeats() {
		ReservationDetailsDTO resdto = new ReservationDetailsDTO();
		resdto.setColumn(1);
		resdto.setRow(1);
		resdto.setManifestationDayId(-1L);
		resdto.setManifestationSectionId(-1L);
		
		reservationDTO.getReservationDetails().add(resdto);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = NoSuchSectionException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoSuchSection() {
		ReservationDetailsDTO resdto = new ReservationDetailsDTO();
		resdto.setColumn(6);
		resdto.setRow(6);
		resdto.setManifestationDayId(-1L);
		resdto.setManifestationSectionId(-20L);
		
		reservationDTO.getReservationDetails().add(resdto);
		
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = NoSuchManifestationException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_NoSuchManifestation() {
		reservationDTO.setManifestationId(-20L);
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test(expected = EmptyReservationDetailsException.class)
	@Rollback
	@Transactional
	public void test_makeReservation_EmptyReservationDetails() {
		reservationDTO.setReservationDetails(null);
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		reservationService.makeReservation(reservationDTO);
	}
	
	@Test
	@Rollback
	@Transactional
	public void test_makeReservation_Normal() {
		List<Reservation> allReservations = reservationRepository.findAll();
		SecurityContextHolder.getContext().setAuthentication(this.authentication);
		MakeReservationResponseDTO makeDTO = reservationService.makeReservation(reservationDTO);
		
		assertEquals(5, allReservations.size() + 1);
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
		double expected = 700;
		double actual = reservationService.getExpectedTotalPriceForManifestationDay(-1L);
		assertEquals(expected, actual);
	}
	
	@Test
	public void test() {
		assertEquals(1, 1);
	}
	
	
}
