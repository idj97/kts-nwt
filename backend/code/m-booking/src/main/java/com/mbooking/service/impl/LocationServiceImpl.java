package com.mbooking.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.PageRequestDTO;
import com.mbooking.exception.ApiException;
import com.mbooking.model.Layout;
import com.mbooking.model.Location;
import com.mbooking.repository.LayoutRepository;
import com.mbooking.repository.LocationRepository;
import com.mbooking.service.LocationService;

@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private LayoutRepository layoutRepo;
	
	@Override
	public List<LocationDTO> getLocations(PageRequestDTO pageRequest) {
		Pageable pageable = PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize());
		return locationRepo.findAll(pageable).map(loc -> new LocationDTO(loc)).toList();
	}
	
	@Override
	public List<LocationDTO> getByNameOrAddress(String name, String address) {
		return locationRepo.findByNameContainingAndAddressContaining(name, address)
				.stream().map(loc -> new LocationDTO(loc)).collect(Collectors.toList());
	}

	@Override
	public void createLocation(LocationDTO locationDTO) {
		Optional<Layout> layout = layoutRepo.findById(locationDTO.getLayoutId());
		if (layout.isPresent()) {
			Location location = new Location();
			location.setName(locationDTO.getName());
			location.setAddress(locationDTO.getAddress());
			location.setLayout(layout.get());
			locationRepo.save(location);
		} else {
			throw new ApiException("Invalid location id", HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void updateLocation(Long id, LocationDTO locationDTO) {
		Optional<Location> opt = locationRepo.findById(id);
		if (opt.isPresent()) {
			Location location = opt.get();
			location.setName(locationDTO.getName());
			location.setAddress(locationDTO.getAddress());
			//TODO: Can we change location layout when some manifestations already use location?
			locationRepo.save(location);
		} else {
			throw new ApiException("Invalid location id", HttpStatus.NOT_FOUND);
		}
	}
}
