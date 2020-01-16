package com.mbooking.service.impl;

import com.mbooking.dto.LayoutDTO;
import com.mbooking.exception.ApiNotFoundException;
import com.mbooking.model.Layout;
import com.mbooking.repository.LayoutRepository;
import com.mbooking.service.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LayoutServiceImpl implements LayoutService {

    @Autowired
    private LayoutRepository layoutRepository;

    @Override
    public LayoutDTO getById(Long id) {
        Optional<Layout> opt = layoutRepository.findById(id);
        if (opt.isPresent()) {
            return new LayoutDTO(opt.get());
        }
        throw new ApiNotFoundException("Layout not exist.");
    }

    @Override
    public List<LayoutDTO> getByName(String name) {
        return layoutRepository.findByNameContaining(name)
                .stream()
                .map(LayoutDTO::new)
                .collect(Collectors.toList());
    }
}
