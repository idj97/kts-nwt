package com.mbooking.repository;

import com.mbooking.model.ManifestationImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManifestationImageRepository extends JpaRepository<ManifestationImage, Long> {

    List<ManifestationImage> findByManifestationId(Long manifestationId);
}
