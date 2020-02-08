package com.mbooking.dto.reports;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class LocationReportRequestDTO {
    private Long locationId;
    private Date startDate;
    private Date endDate;
}
