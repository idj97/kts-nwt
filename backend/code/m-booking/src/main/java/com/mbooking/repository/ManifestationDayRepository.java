package com.mbooking.repository;

import com.mbooking.model.ManifestationDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ManifestationDayRepository extends JpaRepository<ManifestationDay, Long> {

    @Modifying
    @Query("delete from ManifestationDay md where md.id=?1")
    void deleteById(Long id);

    ManifestationDay findByIdAndManifestationId(Long id, Long manifestationId);


}


