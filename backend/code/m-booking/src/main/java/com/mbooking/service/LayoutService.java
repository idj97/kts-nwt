package com.mbooking.service;

import com.mbooking.dto.LayoutDTO;

import java.util.List;
import java.util.Map;

public interface LayoutService {
    LayoutDTO getById(Long id);
    List<LayoutDTO> getByName(String name);
    Map<String, Long> getNameIdMappings();
}
