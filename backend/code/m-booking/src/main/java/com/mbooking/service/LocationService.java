package com.mbooking.service;

import java.util.List;

import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.PageRequestDTO;

public interface LocationService {
	void createLocation(LocationDTO locationDTO);
	void updateLocation(Long id, LocationDTO locationDTO);
	List<LocationDTO> getLocations(PageRequestDTO pageRequest);
	List<LocationDTO> getByNameOrAddress(String name, String address);
}
