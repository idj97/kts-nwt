package com.mbooking.controller;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.model.Manifestation;
import com.mbooking.service.ManifestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/manifest")
public class ManifestationController {

    @Autowired
    ManifestationService manifestSvc;

    @GetMapping(value="/getAll")
    @Secured({ "ROLE_SYS_ADMIN", "ROLE_ADMIN"})
    public ResponseEntity<List<Manifestation>> getAllManifestations() {
        return new ResponseEntity<>(manifestSvc.findAll(), HttpStatus.OK);
    }

    @GetMapping(value="/search")
    public ResponseEntity<List<ManifestationDTO>> searchManifestations(
            @RequestParam(defaultValue = "") String name, @RequestParam(defaultValue = "") String type,
            @RequestParam(defaultValue = "") String locationName) {
        return new ResponseEntity<>(manifestSvc.searchManifestations(name, type, locationName), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @Secured({ "ROLE_SYS_ADMIN", "ROLE_ADMIN"})
    public ResponseEntity<Manifestation> createNewManifestation(@Valid @RequestBody ManifestationDTO newManifestData) {

        return new ResponseEntity<>(manifestSvc.createManifestation(newManifestData), HttpStatus.ACCEPTED);
    }

    @PostMapping(value="/update")
    @Secured({"ROLE_SYS_ADMIN", "ROLE_ADMIN"})
    public ResponseEntity<Manifestation> updateManifestation(@Valid @RequestBody ManifestationDTO manifestData) {

        return new ResponseEntity<>(manifestSvc.updateManifestation(manifestData), HttpStatus.ACCEPTED);
    }

}
