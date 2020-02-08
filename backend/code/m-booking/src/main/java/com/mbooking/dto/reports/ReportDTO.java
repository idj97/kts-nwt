package com.mbooking.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long manifestationCount;
    private Long ticketCount;
    private Long income;
    private List<String> labels;
    private List<Long> data;
}
