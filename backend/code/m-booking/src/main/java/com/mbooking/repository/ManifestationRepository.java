package com.mbooking.repository;

import com.mbooking.model.Manifestation;
import com.mbooking.model.ManifestationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManifestationRepository extends JpaRepository<Manifestation, Long> {

    List<Manifestation> findByNameContainingAndManifestationTypeAndLocationNameContaining(
            String name, ManifestationType manifestationType, String locationName);

    List<Manifestation> findByNameContainingAndLocationNameContaining(String name, String locationName);
}
