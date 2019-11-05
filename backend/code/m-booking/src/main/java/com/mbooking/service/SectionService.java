package com.mbooking.service;

import com.mbooking.model.Section;

import java.util.Optional;

public interface SectionService {

    Optional<Section> findById(Long sectionId);
}
