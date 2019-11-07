package com.mbooking.repository;

import com.mbooking.model.ManifestationSection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ManifestationSectionRepository extends JpaRepository<ManifestationSection, Long> {

    @Modifying
    @Query("delete from ManifestationSection ms where ms.id=?1")
    void deleteById(Long id);
}
