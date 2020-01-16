package com.mbooking.controller;

import com.mbooking.dto.LayoutDTO;
import com.mbooking.service.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/layouts")
public class LayoutController {

    @Autowired
    private LayoutService layoutService;

    @GetMapping("/{id}")
    public ResponseEntity<LayoutDTO> getById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(layoutService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/{name}")
    public ResponseEntity<List<LayoutDTO>> getByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(layoutService.getByName(name), HttpStatus.OK);
    }
}
