package ru.javawebinar.topjava.util;

import java.time.LocalTime;

public class Constants {

    public static final int CALORIES_PER_DAY = 2000;
    public static final LocalTime START_OF_DAY = LocalTime.of(0, 0);
    public static final LocalTime END_OF_DAY = LocalTime.of(23, 59);

    private Constants() throws Exception {}
}
