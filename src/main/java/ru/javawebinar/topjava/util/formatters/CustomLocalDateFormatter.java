package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomLocalDateFormatter implements Formatter<LocalDate> {

    private final static String PATTERN = "yyyy-MM-dd";

    @Override
    public LocalDate parse(String text, Locale locale) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(PATTERN));
    }

    @Override
    public String print(LocalDate localDate, Locale locale) {
        return localDate.toString();
    }
}
