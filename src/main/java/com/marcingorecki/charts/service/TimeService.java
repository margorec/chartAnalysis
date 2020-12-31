package com.marcingorecki.charts.service;

import com.marcingorecki.charts.domain.Period;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class TimeService {

    private static final DateTimeFormatter FORMATTER  = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String getBackwardDate(String period) {
        return getBackwardDateFrom(Period.valueOf(period), LocalDate.now());
    }

    public String getBackwardDateFrom(Period period, LocalDate date) {
        return FORMATTER.format(date.minus(period.days(), ChronoUnit.DAYS));
    }

    public String parseDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        return date.format(FORMATTER);
    }

}
