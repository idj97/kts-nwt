package com.mbooking.repository;

import com.mbooking.model.Manifestation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManifestationRepository extends JpaRepository<Manifestation, Long> {

    List<Manifestation> findByLocationId(Long locationId);
}
