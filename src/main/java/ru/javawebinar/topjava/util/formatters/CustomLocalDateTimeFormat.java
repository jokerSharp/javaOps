package ru.javawebinar.topjava.util.formatters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomLocalDateTimeFormat {

    Type type();

    enum Type {
        DATE,
        TIME
    }
}
