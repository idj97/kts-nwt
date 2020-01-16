package com.mbooking.dto;

import com.mbooking.model.Section;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SectionDTO {
    private Long id;
    private String name;
    private boolean isSeating;
    private int sectionRows;
    private int sectionColumns;

    public SectionDTO(Section section) {
        super();
        id = section.getId();
        name = section.getName();
        isSeating = section.isSeating();
        sectionRows = section.getSectionRows();
        sectionColumns = section.getSectionColumns();
    }
}
