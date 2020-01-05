package com.mbooking.controller;

import com.mbooking.dto.LocationDTO;
import com.mbooking.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(locationService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getByNameOrAddress(
            @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String address,
            @RequestParam(defaultValue = "0") int pageNum, @RequestParam(defaultValue = "20") int pageSize) {
        return new ResponseEntity<>(locationService.getByNameOrAddress(name, address, pageNum, pageSize), HttpStatus.OK);
    }

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<LocationDTO> createLocation(@Valid @RequestBody LocationDTO locationDTO) {
        return new ResponseEntity<>(locationService.createLocation(locationDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable("id") Long id, @Valid @RequestBody LocationDTO locationDTO) {
        return new ResponseEntity<>(locationService.updateLocation(id, locationDTO), HttpStatus.OK);
    }

}
