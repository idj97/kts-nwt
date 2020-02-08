package com.mbooking.controller;

import com.mbooking.dto.LayoutDTO;
import com.mbooking.service.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    @Autowired
    private LayoutService layoutService;

    @GetMapping("/{id}")
    public ResponseEntity<LayoutDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(layoutService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LayoutDTO>> getByName(@RequestParam(defaultValue = "") String name) {
        return new ResponseEntity<>(layoutService.getByName(name), HttpStatus.OK);
    }

    @GetMapping("/mappings")
    public ResponseEntity<Map<String, Long>> getNameIdMappings() {
        return new ResponseEntity<>(layoutService.getNameIdMappings(), HttpStatus.OK);
    }
}
