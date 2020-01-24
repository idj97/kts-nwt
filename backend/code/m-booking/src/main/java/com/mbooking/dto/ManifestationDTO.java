package com.mbooking.dto;

import com.mbooking.model.*;
import com.mbooking.utility.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ManifestationDTO {

    private Long manifestationId;

    @NotNull(message = "Manifestation name is required")
    @Size(max = Constants.NAME_LENGTH,
            message = "Name can't contain more than " + Constants.NAME_LENGTH + " characters")
    private String name;

    @NotNull(message = "Manifestation description is required")
    @Size(max = Constants.DESCR_LENGTH,
            message="Description can't contain more than " + Constants.DESCR_LENGTH + " characters")
    private String description;

    @NotNull(message = "Manifestation type is required")
    private ManifestationType type;

    @Max(value=10, message="Maximum reservations per user can't be more than 10")
    private int maxReservations;

    @NotNull(message = "The manifestation must contain at least 1 date")
    private List<Date> manifestationDates;
    
    private List<Long> manifestationDaysId;

    @Future(message = "The last day of the reservations has to be a future date")
    private Date reservableUntil;

    @NotNull(message = "Please specify whether the reservations for a manifestation are allowed")
    private boolean reservationsAllowed;

    private List<ManifestationImageDTO> images;

    //@NotNull(message = "Please select the location sections you would like to include")
    List<ManifestationSectionDTO> selectedSections;

    @NotNull(message = "The location for the manifestation is required")
    private Long locationId;

    private String locationName;

    public ManifestationDTO(Manifestation manifestation) {

        this.manifestationId = manifestation.getId();
        this.name = manifestation.getName();
        this.description = manifestation.getDescription();
        this.reservationsAllowed = manifestation.isReservationsAvailable();
        this.type = manifestation.getManifestationType();
        this.locationName = manifestation.getLocation().getName();
        this.locationId = manifestation.getLocation().getId();
        this.reservableUntil = manifestation.getReservableUntil();
        this.maxReservations = manifestation.getMaxReservations();

        this.manifestationDates = new ArrayList<>();
        this.manifestationDaysId = new ArrayList<>();
        for(ManifestationDay manifDay: manifestation.getManifestationDays()) {
            System.out.println("Adding day: " + manifDay.getDate());
            this.manifestationDates.add(manifDay.getDate());
            this.manifestationDaysId.add(manifDay.getId());
        }

        this.images = new ArrayList<>();
        for(ManifestationImage image: manifestation.getImages()) {
            this.images.add(new ManifestationImageDTO(image));
        }

        this.selectedSections = new ArrayList<>();
        for(ManifestationSection selectedSection: manifestation.getSelectedSections()){
            this.selectedSections.add(new ManifestationSectionDTO(selectedSection));
        }

    }
}
