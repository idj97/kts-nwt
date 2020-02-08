package com.mbooking.service.impl;

import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.ResultsDTO;
import com.mbooking.exception.ApiBadRequestException;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Layout;
import com.mbooking.model.Location;
import com.mbooking.model.Manifestation;
import com.mbooking.repository.LayoutRepository;
import com.mbooking.repository.LocationRepository;
import com.mbooking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private LayoutRepository layoutRepo;

	@Override
	public List<LocationDTO> getAllLocations() {
		return locationRepo.findAll()
				.stream()
				.map(location -> new LocationDTO(location))
				.collect(Collectors.toList());
	}
	
	@Override
	public LocationDTO getById(Long id) {
		Optional<Location> opt = locationRepo.findById(id);
		if (opt.isPresent()) {
			return new LocationDTO(opt.get());
		} else {
			throw new ApiNotFoundException("Location doesnt exist.");
		}
	}

	@Override
	public ResultsDTO<LocationDTO> getByNameOrAddress(String name, String address, int pageNum, int pageSize) {
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		Page<Location> locationPage = locationRepo.findByNameContainingAndAddressContaining(name, address, pageable);

		List<LocationDTO> locationDTOS = locationPage
				.stream()
				.map(loc -> new LocationDTO(loc))
				.collect(Collectors.toList());

		return new ResultsDTO(locationDTOS, locationPage.getTotalPages());
	}

	@Override
	public LocationDTO createLocation(LocationDTO locationDTO) {
		Optional<Layout> layout = layoutRepo.findById(locationDTO.getLayoutId());
		if (layout.isPresent()) {
			Location location = new Location(locationDTO.getName(), locationDTO.getAddress(), layout.get());
			location = locationRepo.save(location);
			return new LocationDTO(location);
		} else {
			throw new ApiNotFoundException("Layout not found.");
		}
	}
		
	@Override
	public LocationDTO updateLocation(Long locationId, LocationDTO locationDTO) {
		Optional<Location> optLocation = locationRepo.findById(locationId);
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

	@Override
	public void delete(Long locationId) {
		Optional<Location> optionalLocation = locationRepo.findById(locationId);
		if (optionalLocation.isPresent()) {
			Location location = optionalLocation.get();
			checkIfUpdateIsPossible(location);
			location.setName("deleted_" + location.getName());
			location.setDeleted(true);
			locationRepo.save(location);
		} else {
			throw new ApiNotFoundException("Location not found.");
		}
	}

	//If location have at least one unfinished manifestation layout change is not possible
	public void checkIfUpdateIsPossible(Location location) {
		for (Manifestation manf : location.getManifestations()) {
			int lastIndex = manf.getManifestationDays().size()-1;
			Date lastDate = manf.getManifestationDays().get(lastIndex).getDate();
			if (lastDate.after(new Date())) {
				throw new ApiBadRequestException("This location have unfinished manifestations.");
			}
		}
	}
 }
