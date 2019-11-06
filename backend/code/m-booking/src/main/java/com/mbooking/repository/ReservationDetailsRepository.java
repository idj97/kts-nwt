package com.mbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.ReservationDetails;

public interface ReservationDetailsRepository extends JpaRepository<ReservationDetails, Long> {
	ReservationDetails findByManifestationSectionIdAndRowAndColumn(Long id, int row, int column);
}