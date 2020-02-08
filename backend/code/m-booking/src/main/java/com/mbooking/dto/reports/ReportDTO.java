package com.mbooking.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class
ReportDTO {
    private Long ticketCount = 0L;
    private Double income = 0.0;
    private List<String> labels = new ArrayList<>();
    private List<Long> ticketData = new ArrayList<>();
    private List<Double> incomeData = new ArrayList<>();
}
