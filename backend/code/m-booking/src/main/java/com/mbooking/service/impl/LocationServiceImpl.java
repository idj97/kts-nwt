package com.mbooking.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mbooking.dto.LocationDTO;
import com.mbooking.exception.ApiException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Layout;
import com.mbooking.model.Location;
import com.mbooking.model.Manifestation;
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
	public LocationDTO getById(Long id) {
		Optional<Location> opt = locationRepo.findById(id);
		if (opt.isPresent()) {
			return new LocationDTO(opt.get());
		} else {
			throw new ApiNotFoundException();
		}
	}
	
	@Override
	public List<LocationDTO> getByNameOrAddress(String name, String address, int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		return locationRepo.findByNameContainingAndAddressContaining(name, address, pageable)
				.stream().map(loc -> new LocationDTO(loc)).collect(Collectors.toList());
	}	
		
	@Override
	public LocationDTO createLocation(LocationDTO locationDTO) {
		Optional<Layout> layout = layoutRepo.findById(locationDTO.getLayoutId());
		if (layout.isPresent()) {
			Location location = new Location();
			location.setName(locationDTO.getName());
			location.setAddress(locationDTO.getAddress());
			location.setLayout(layout.get());
			location = locationRepo.save(location);
			return new LocationDTO(location);
		} else {
			throw new ApiNotFoundException("Layout not found.");
		}
	}	
		
	@Override
	public LocationDTO updateLocation(Long id, LocationDTO locationDTO) {
		Optional<Location> optLocation = locationRepo.findById(id);
		Optional<Layout> optLayout = layoutRepo.findById(locationDTO.getLayoutId());
		
		if (optLocation.isPresent() && optLayout.isPresent()) {
			checkIfUpdateIsPossible(optLocation.get());
			Location location = optLocation.get();
			location.setName(locationDTO.getName());
			location.setAddress(locationDTO.getAddress());
			location.setLayout(optLayout.get());
			location = locationRepo.save(location);
			return new LocationDTO(location);
		} else {
			throw new ApiNotFoundException("Location/layout not found.");
		}
	}	
	
	private void checkIfUpdateIsPossible(Location location) {
		for (Manifestation manf : location.getManifestations()) {
			int lastIndex = manf.getManifestationDays().size()-1;
			Date lastDate = manf.getManifestationDays().get(lastIndex).getDate();
			if (lastDate.after(new Date())) {
				throw new ApiException("You can't change layout.", HttpStatus.BAD_REQUEST);
			}
		}
	}
 }
