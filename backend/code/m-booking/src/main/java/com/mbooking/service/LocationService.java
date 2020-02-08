package com.mbooking.service;

import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.ResultsDTO;

import java.util.List;

public interface LocationService {
	List<LocationDTO> getAllLocations();
	LocationDTO createLocation(LocationDTO locationDTO);
	LocationDTO updateLocation(Long locationId, LocationDTO locationDTO);
	LocationDTO getById(Long id);
	ResultsDTO<LocationDTO> getByNameOrAddress(String name, String address, int pageNum, int pageSize);
	void delete(Long locationId);
}
