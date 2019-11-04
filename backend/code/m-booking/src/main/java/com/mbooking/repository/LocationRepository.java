package com.mbooking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mbooking.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
	List<Location> findByNameContaining(String name);
	List<Location> findByNameContainingAndAddressContaining(String name, String address);
}
