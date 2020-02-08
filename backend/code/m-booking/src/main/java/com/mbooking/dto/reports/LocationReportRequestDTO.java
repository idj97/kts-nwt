package com.mbooking.dto.reports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationReportRequestDTO {
    private Long locationId;
    private String startDate;
    private String endDate;
}
