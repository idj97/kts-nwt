package com.mbooking.service;

import com.mbooking.dto.LocationDTO;

import java.util.List;

public interface LocationService {
	List<LocationDTO> getAllLocations();
	LocationDTO createLocation(LocationDTO locationDTO);
	LocationDTO updateLocation(Long locationId, LocationDTO locationDTO);
	LocationDTO getById(Long id);
	List<LocationDTO> getByNameOrAddress(String name, String address, int pageNum, int pageSize);
}
