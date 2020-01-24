package com.mbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.Customer;
import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationDay;
import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Long> {
	ReservationDetails findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
			Long id, int row, int column, Long dayId, List<ReservationStatus> status);
	
	List<ReservationDetails> findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
			Long id, Long dayId, List<ReservationStatus> status);
	
	List<ReservationDetails> findByManifestationDayId(Long id);
	
	List<ReservationDetails> findByReservationCustomerAndManifestationDayAndReservationManifestationAndReservationStatusNotIn(
			Customer customer, ManifestationDay manifestationDay, Manifestation manifestation, List<ReservationStatus> status);
	
	List<ReservationDetails> findByManifestationDayAndReservationManifestationAndReservationStatusNotIn(
			ManifestationDay manifestationDay, Manifestation manifestation, List<ReservationStatus> status);
}