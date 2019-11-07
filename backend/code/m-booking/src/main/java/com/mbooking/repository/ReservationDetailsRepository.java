package com.mbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.ReservationDetails;
import com.mbooking.model.ReservationStatus;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Long> {
	ReservationDetails findByManifestationSectionIdAndRowAndColumnAndManifestationDayIdAndReservationStatusNotIn(
			Long id, int row, int column, Long dayId, List<ReservationStatus> status);
	
	List<ReservationDetails> findByManifestationSectionIdAndManifestationDayIdAndIsSeatingFalseAndReservationStatusNotIn(
			Long id, Long dayId, List<ReservationStatus> status);
	
	List<ReservationDetails> findByManifestationDayId(Long id);
}