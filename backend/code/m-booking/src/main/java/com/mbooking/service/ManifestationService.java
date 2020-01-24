package com.mbooking.service;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.dto.ManifestationImageDTO;
import com.mbooking.model.Manifestation;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ManifestationService {

    ManifestationDTO createManifestation(ManifestationDTO newManifestData);

    ManifestationDTO updateManifestation(ManifestationDTO manifestData);

    List<ManifestationImageDTO> uploadImages(MultipartFile[] files, Long manifestationId);

    Manifestation save(Manifestation manifestation);

    Optional<Manifestation> findOneById(Long id);

    ManifestationDTO getManifestationById(Long id);

    List<ManifestationDTO> findAll(int pageNum, int pageSize);

    List<ManifestationDTO> searchManifestations(String name, String type, String locationName, String date, int pageNum, int pageSize);
}
