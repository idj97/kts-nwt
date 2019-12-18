package com.mbooking.repository;

import com.mbooking.model.Customer;
import com.mbooking.model.Manifestation;
import com.mbooking.model.Reservation;
import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>  {
	List<Reservation> findAll();
	List<Reservation> findAllByCustomer(Customer customer);
	List<Reservation> findAllByCustomerAndManifestation(Customer customer, Manifestation manifestation);
	List<Reservation> findByExpirationDateBeforeAndStatusEquals(Date date, ReservationStatus status);
	List<Reservation> findByManifestationIdAndStatusNotIn(Long id, List<ReservationStatus> status);
	List<Reservation> findDistinctByReservationDetailsInAndStatusIn(
			List<ReservationDetails> reservationDetails, List<ReservationStatus> status);
}
