package com.mbooking.service;

import java.util.List;

import com.mbooking.dto.LocationDTO;

public interface LocationService {
	void createLocation(LocationDTO locationDTO);
	void updateLocation(Long id, LocationDTO locationDTO);
	LocationDTO getById(Long id);
	List<LocationDTO> getByNameOrAddress(String name, String address, int pageNum, int pageSize);
}
