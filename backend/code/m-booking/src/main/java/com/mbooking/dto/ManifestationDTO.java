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

    @NotNull(message = "Manifestation name is required")
    private String name;

    @NotNull(message = "Manifestation description is required")
    private String description;

    @NotNull(message = "Manifestation type is required")
    private ManifestationType type;

    @NotNull(message = "Maximum number of reservations for a single user is required")
    @Positive(message = "The number of maximum reservations must be positive")
    private int maxReservations;

    @NotNull(message = "Start date of the manifestation is required")
    @Future(message = "Future dates are required")
    private Date startDate;

    @NotNull(message = "End date of the manifestation is required")
    @Future(message = "Future dates are required")
    private Date endDate;

    private Date reservableUntil;

    @NotNull(message = "Please specify whether the reservations for a manifestation are allowed")
    private boolean reservationsAllowed;

    private List<String> images;

    List<ManifestationSectionDTO> selectedSections;

    @NotNull(message = "The location for the manifestation is required")
    private Long locationId;
}
