package com.mbooking.dto;

import com.mbooking.model.ManifestationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ManifestationDTO {

    private String name;
    private String description;
    private ManifestationType type;
    private int maxReservations;

    private Date startDate;
    private Date endDate;
    private Date reservableUntil;

    private List<String> images;
    List<ManifestationSectionDTO> selectedSections;

    private boolean reservationsAllowed;

    //TODO: location
}
