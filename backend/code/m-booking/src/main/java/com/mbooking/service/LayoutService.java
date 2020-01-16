package com.mbooking.service;

import com.mbooking.dto.LayoutDTO;

import java.util.List;

public interface LayoutService {
    LayoutDTO getById(Long id);
    List<LayoutDTO> getByName(String name);
}
