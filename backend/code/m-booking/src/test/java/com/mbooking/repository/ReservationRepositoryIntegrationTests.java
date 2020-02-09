package com.mbooking.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.mbooking.model.Customer;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationStatus;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:application-test_reservation.properties")
public class ReservationRepositoryIntegrationTests {

	@Autowired
	private ReservationRepository reservationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManifestationRepository manifestationRepository;
	
	@Autowired
	private ReservationDetailsRepository reservationsDetailsRepository;
	
	@Test
	public void test_findDistinctByReservationDetailsInAndStatusIn() {
		List<Reservation> reservations = reservationRepository.findDistinctByReservationDetailsInAndStatusIn(
				Arrays.asList(reservationsDetailsRepository.findById(1L).get(),
						reservationsDetailsRepository.findById(2L).get()),
				Arrays.asList(ReservationStatus.CREATED));
		
		assertEquals(1, reservations.size());
		
		reservations = reservationRepository.findDistinctByReservationDetailsInAndStatusIn(
				Arrays.asList(reservationsDetailsRepository.findById(1L).get(),
						reservationsDetailsRepository.findById(2L).get(),
						reservationsDetailsRepository.findById(3L).get()),
				Arrays.asList(ReservationStatus.CREATED));
		
		assertEquals(2, reservations.size());
	}
	
	@Test
	public void test_findByManifestationIdAndStatusNotIn() {
		List<Reservation> reservations = reservationRepository.findByManifestationIdAndStatusNotIn(
				-1L, Arrays.asList(ReservationStatus.CANCELED,
						ReservationStatus.CONFIRMED,
						ReservationStatus.EXPIRED));
		
		assertEquals(5, reservations.size());
		
		reservations = reservationRepository.findByManifestationIdAndStatusNotIn(
				-1L, Arrays.asList(ReservationStatus.CANCELED,
						ReservationStatus.CONFIRMED,
						ReservationStatus.EXPIRED,
						ReservationStatus.CREATED));
		
		assertEquals(0, reservations.size());
	}
	
	@Test
	public void test_findByExpirationDateBeforeAndStatusEquals() throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		Date date = format.parse("22.12.2019 00:00");
		List<Reservation> reservations = reservationRepository.findByExpirationDateBeforeAndStatusEquals(
				date, 
				ReservationStatus.CREATED);
		
		assertEquals(5, reservations.size());
		
		date = format.parse("12.12.2019 00:00");
		reservations = reservationRepository.findByExpirationDateBeforeAndStatusEquals(
				date, 
				ReservationStatus.CREATED);
		
		assertEquals(0, reservations.size());
		
	}
	
	@Test
	public void test_findAllByCustomerAndManifestation() {
		List<Reservation> reservations = reservationRepository.findAllByCustomerAndManifestation(
				(Customer)userRepository.findByEmail("ktsnwt.customer@gmail.com"),
				manifestationRepository.findById(-1L).get());
		
		assertEquals(4, reservations.size());
		
		reservations = reservationRepository.findAllByCustomerAndManifestation(
				(Customer)userRepository.findByEmail("ktsnwt.customer@gmail.com"),
				manifestationRepository.findById(-2L).get());
		
		assertEquals(0, reservations.size());
	}
	
	@Test
	public void test_findAllByCustomer() {
		List<Reservation> reservations = reservationRepository.findAllByCustomer(
				(Customer)userRepository.findByEmail("ktsnwt.customer@gmail.com"));
		
		assertEquals(4, reservations.size());
	}
	
	@Test
	public void test_findAll() {
		List<Reservation> reservations = reservationRepository.findAll();
		
		assertEquals(5, reservations.size());
	}
	
}
