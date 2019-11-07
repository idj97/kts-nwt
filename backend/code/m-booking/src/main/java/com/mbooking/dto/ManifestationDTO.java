package com.mbooking.dto;

import com.mbooking.model.ManifestationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ManifestationDTO {

    private Long manifestationId;

    @NotNull(message = "Please enter manifestation name")
    private String name;

    @NotNull(message = "Please enter manifestation description")
    private String description;

    @NotNull(message = "Please select manifestation type")
    private ManifestationType type;

    @NotNull(message = "Please specify the maximum number of reservations for a single user")
    @Positive(message = "The number of maximum reservations must be positive")
    private int maxReservations;

    @NotNull(message = "Please select the start date of the manifestation")
    @Future
    private Date startDate;

    @NotNull(message = "Please select the end date of the manifestation")
    @Future
    private Date endDate;

    private Date reservableUntil;

    @NotNull(message = "Please specify whether the reservations for a manifestation are allowed")
    private boolean reservationsAllowed;

    private List<String> images;

    List<ManifestationSectionDTO> selectedSections;

    @NotNull(message = "Please select the location for the manifestation")
    private Long locationId;
}
