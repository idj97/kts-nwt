package com.mbooking.controller;

import com.mbooking.dto.ManifestationDTO;
import com.mbooking.model.Manifestation;
import com.mbooking.service.ManifestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/manifest")
public class ManifestationController {

    @Autowired
    ManifestationService manifestSvc;

    @PostMapping(value = "/create")
    @Secured({ "ROLE_SYS_ADMIN", "ROLE_ADMIN"})
    public ResponseEntity<Manifestation> createNewManifestation(@Valid @RequestBody ManifestationDTO newManifestData) {
        System.out.println("Creating manifest");
        return new ResponseEntity<>(manifestSvc.createManifestation(newManifestData), HttpStatus.ACCEPTED);
    }
}
