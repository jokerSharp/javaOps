package ru.javawebinar.topjava.util.formatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomLocalDateTimeFormatAnnotationFormatterFactory
        implements AnnotationFormatterFactory<CustomLocalDateTimeFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(CustomLocalDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(CustomLocalDateTimeFormat annotation, Class<?> fieldType) {
        return getFormatter(annotation, fieldType);
    }

    private Formatter<?> getFormatter(CustomLocalDateTimeFormat annotation, Class<?> fieldType) {
        switch (annotation.type()) {
            case DATE -> {
                return new CustomLocalDateFormatter();
            }
            case TIME -> {
                return new CustomLocalTimeFormatter();
            }
        }
        return null;
    }
}
