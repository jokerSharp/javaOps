package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomLocalTimeFormatter implements Formatter<LocalTime> {

    private final static String PATTERN = "HH:mm";

    @Override
    public LocalTime parse(String text, Locale locale) {
        return LocalTime.parse(text, DateTimeFormatter.ofPattern(PATTERN));
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        return localTime.toString();
    }
}
