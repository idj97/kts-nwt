package com.mbooking.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	Page<Location> findByNameContainingAndAddressContaining(String name, String address, Pageable pageable);
}
