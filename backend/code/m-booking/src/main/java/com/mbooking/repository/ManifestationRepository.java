package com.mbooking.repository;

import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ManifestationRepository extends JpaRepository<Manifestation, Long> {

    // used when looking up manifestations with same date and location
    List<Manifestation> findDistinctByLocationIdAndManifestationDaysDateNoTimeIn(Long locationId, List<Date> dates);

    /** Manifestation search queries **/

    // search with name, location, and date
    List<Manifestation> findDistinctByNameContainingAndLocationNameContainingAndManifestationDaysDateBetween(
            String name, String locationName, Date searchDateStart, Date searchDateEnd, Pageable pageable);

    // search with all parameters -> name, type, location and date
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
