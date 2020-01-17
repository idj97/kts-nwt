package com.mbooking.dto;

import com.mbooking.model.Layout;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class LayoutDTO {
    private Long id;
    private String name;
    private List<SectionDTO> sections;

    public LayoutDTO(Layout layout) {
        super();
        id = layout.getId();
        name = layout.getName();
        sections = layout.getSections().stream().map(SectionDTO::new).collect(Collectors.toList());
    }
}
