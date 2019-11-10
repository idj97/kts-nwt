package com.mbooking.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
public class ManifestationSectionDTO {

    @NotNull(message = "At least one section is required")
    private Long sectionID;

    @NotNull(message = "The size you would like to use for the selected section is required")
    @Positive(message = "The size for the section must be positive")
    private int size;

    @NotNull(message = "The ticket price for the selected section is required")
    @Positive(message = "The price for the section must be positive")
    private double price;
}
