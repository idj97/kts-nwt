package com.mbooking.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.model.Customer;
import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class ReservationDetailsRepositoryIntegrationTests {
	
	@Autowired
	private ReservationDetailsRepository reservationDetailsRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManifestationDayRepository manifestationDayRepository;
	
	@Autowired
	private ManifestationRepository manifestationRepository;
	
	@Test
	public void test_findByReservationCustomerAndManifestationDayAndReservationManifestation() {
		List<ReservationDetails> reservationDetails = reservationDetailsRepository.findByReservationCustomerAndManifestationDayAndReservationManifestation(
				(Customer)userRepository.findByEmail("ktsnwt.customer@gmail.com"),
				manifestationDayRepository.findById(-1L).get(),
				manifestationRepository.findById(-1L).get());
		
		assertEquals(7, reservationDetails.size());
		
		reservationDetails = reservationDetailsRepository.findByReservationCustomerAndManifestationDayAndReservationManifestation(
				(Customer)userRepository.findByEmail("ktsnwt.customer@gmail.com"),
				manifestationDayRepository.findById(-2L).get(),
				manifestationRepository.findById(-1L).get());
		
		assertEquals(0, reservationDetails.size());
	}
	
	@Test
	public void test_findByManifestationDayId() {
		List<ReservationDetails> reservationDetails = reservationDetailsRepository.findByManifestationDayId(-1L);
		assertEquals(7, reservationDetails.size());
		
		reservationDetails = reservationDetailsRepository.findByManifestationDayId(-2L);
		assertEquals(0, reservationDetails.size());
	}
	
	@Test
	public void test_findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn() {
		List<ReservationDetails> reservationDetails = reservationDetailsRepository.findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
				-2L, -1L, Arrays.asList(ReservationStatus.CANCELED,
						ReservationStatus.EXPIRED));
		
		assertEquals(1, reservationDetails.size());
		
		reservationDetails = reservationDetailsRepository.findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
				-1L, -1L, Arrays.asList(ReservationStatus.CANCELED,
						ReservationStatus.EXPIRED));
		
		assertEquals(0, reservationDetails.size());
	}
	
	@Test
	public void test_findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn() {
		ReservationDetails reservationDetails = reservationDetailsRepository.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
				-1L, 2, 2, -1L, Arrays.asList(
						ReservationStatus.CANCELED,
						ReservationStatus.EXPIRED));
		
		assertNotNull(reservationDetails);
		
		reservationDetails = reservationDetailsRepository.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
				-1L, 1, 1, -1L, Arrays.asList(
						ReservationStatus.CANCELED,
						ReservationStatus.EXPIRED));
		
		assertNull(reservationDetails);
		
		reservationDetails = reservationDetailsRepository.findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
				-1L, 2, 2, -1L, Arrays.asList(
						ReservationStatus.CANCELED,
						ReservationStatus.EXPIRED,
						ReservationStatus.CREATED));
		
		assertNull(reservationDetails);
	}

}
