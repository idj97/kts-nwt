package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.model.Manifestation;

public interface ManifestationService {

    Manifestation createManifestation(ManifestationDTO newManifestData);
}
