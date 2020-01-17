package com.mbooking.repository;

import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ManifestationRepository extends JpaRepository<Manifestation, Long> {

    List<Manifestation> findByLocationId(Long locationId);


    //List<Manifestation> findDistinctByLocationIdAndManifestationDaysDateEquals(Long locationId, Date )

    List<Manifestation> findDistinctByManifestationDaysDateAfter(Date currentDate, Pageable pageable);


    /* Manifestation search queries */

    // search with name, location, and date
    List<Manifestation> findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateBetween(
            String name, String locationName, Date searchDateStart, Date searchDateEnd, Pageable pageable);

    // search with name, type, location and date
    List<Manifestation> findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateBetween(
            String name, ManifestationType manifestationType, String locationName,
            Date searchDateStart, Date searchDateEnd, Pageable pageable);

    // search with name, type and location returning all future manifestations
    List<Manifestation> findDistinctByNameContainingAndManifestationTypeAndLocationNameContainingAndManifestationDaysDateAfter(
            String name, ManifestationType manifestationType, String locationName, Date currentDate,
            Pageable pageable);

    // search with name and location returning all future manifestations
    List<Manifestation> findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateAfter(
            String name, String locationName, Date currentDate, Pageable pageable);

}
