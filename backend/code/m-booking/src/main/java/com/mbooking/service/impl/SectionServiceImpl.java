package com.mbooking.service.impl;

import com.mbooking.model.Section;
import com.mbooking.repository.SectionRepository;
import com.mbooking.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    SectionRepository sectionRepo;

    public Optional<Section> findById(Long sectionId) {
        return sectionRepo.findById(sectionId);
    }
}
