package com.mbooking.service.impl;

import com.mbooking.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class DateService {

    public Comparator<String> getComparator() {
        return (dateOne, dateTwo) -> parseDate(dateOne).compareTo(parseDate(dateTwo));
    }

    public LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public boolean isInsideDates(Reservation reservation, List<String> localDates) {
        String rDate = formatDate(toLocalDate(reservation.getDateCreated()));
        return localDates.contains(rDate);
    }

    public boolean isInsideDatesWithDay(Reservation reservation, List<String> localDates) {
        String rDate = formatDateWithDay(toLocalDate(reservation.getDateCreated()));
        return localDates.contains(rDate);
    }

    public List<String> generateDatesPerMonth(LocalDate startDate, LocalDate endDate) {
        List<String> localDates = new ArrayList<>();
        for (LocalDate tmpDate = startDate; tmpDate.isBefore(endDate); tmpDate = tmpDate.plusMonths(1)) {
            localDates.add(formatDate(tmpDate));
        }
        return localDates;
    }

    public List<String> generateDatesPerDay(LocalDate startDate, LocalDate endDate) {
        List<String> localDates = new ArrayList<>();
        for (LocalDate tmpDate = startDate; tmpDate.isBefore(endDate); tmpDate = tmpDate.plusDays(1)) {
            localDates.add(formatDateWithDay(tmpDate));
        }
        return localDates;
    }

    public String formatDate(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("MMM-yy"));
    }

    public String formatDateWithDay(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("dd-MMM-yy"));
    }

    public LocalDate parseDate(String str) {
        return LocalDate.parse("01-" + str, DateTimeFormatter.ofPattern("dd-MMM-yy"));
    }

}
