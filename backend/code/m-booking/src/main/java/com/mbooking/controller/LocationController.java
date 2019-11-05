package com.mbooking.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mbooking.dto.LocationDTO;
import com.mbooking.dto.PageRequestDTO;
import com.mbooking.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping
	public ResponseEntity<List<LocationDTO>> getLocations(@Valid @RequestBody PageRequestDTO pageRequest) {
		return new ResponseEntity<>(locationService.getLocations(pageRequest), HttpStatus.OK);
	}

	@GetMapping("/search")
	public ResponseEntity<List<LocationDTO>> getByNameOrAddress(@RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String address) {
		return new ResponseEntity<>(locationService.getByNameOrAddress(name, address), HttpStatus.OK);
	}

	@PostMapping
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<HttpStatus> createLocation(@Valid @RequestBody LocationDTO locationDTO) {
		locationService.createLocation(locationDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/{id}")
	@Secured({"ROLE_ADMIN"})
	public ResponseEntity<HttpStatus> updateLocation(@PathVariable("id") Long id, @Valid @RequestBody LocationDTO locationDTO) {
		locationService.updateLocation(id, locationDTO);
		return new ResponseEntity<>(HttpStatus.OK);
	}

}
