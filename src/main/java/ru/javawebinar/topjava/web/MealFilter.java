package ru.javawebinar.topjava.web;

import java.time.LocalDate;
import java.time.LocalTime;

public class MealFilter {
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final LocalTime timeFrom;
    private final LocalTime timeTo;

    public MealFilter(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }
}
