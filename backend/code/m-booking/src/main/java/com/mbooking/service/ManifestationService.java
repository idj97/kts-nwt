package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.model.Manifestation;

import java.util.List;
import java.util.Optional;

public interface ManifestationService {

    Manifestation createManifestation(ManifestationDTO newManifestData);

    Manifestation updateManifestation(ManifestationDTO manifestData);

    Manifestation save(Manifestation manifestation);

    Optional<Manifestation> findOneById(Long id);

    List<Manifestation> findAll();

    List<ManifestationDTO> searchManifestations(String name, String type, String locationName);
}
