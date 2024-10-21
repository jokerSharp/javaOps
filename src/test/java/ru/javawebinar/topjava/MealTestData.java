package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_1_ID = START_SEQ + 3;
    public static final int USER_MEAL_2_ID = START_SEQ + 4;
    public static final int USER_MEAL_3_ID = START_SEQ + 5;
    public static final int USER_MEAL_4_ID = START_SEQ + 6;
    public static final int USER_MEAL_5_ID = START_SEQ + 7;
    public static final int USER_MEAL_6_ID = START_SEQ + 13;
    public static final int ADMIN_MEAL_ID = START_SEQ + 8;
    public static final int MEAL_ID_NOT_FOUND = 100;

    public static final Meal userMeal1 = new Meal(USER_MEAL_1_ID, LocalDateTime.of(2024, Month.MAY, 30, 23, 12), "user meal1", 470);
    public static final Meal userMeal2 = new Meal(USER_MEAL_2_ID, LocalDateTime.of(2024, Month.MAY, 29, 6, 5), "user meal2", 570);
    public static final Meal userMeal3 = new Meal(USER_MEAL_3_ID, LocalDateTime.of(2024, Month.MAY, 28, 15, 16), "user meal3", 300);
    public static final Meal userMeal4 = new Meal(USER_MEAL_4_ID, LocalDateTime.of(2024, Month.FEBRUARY, 16, 18, 24), "user meal4", 1100);
    public static final Meal userMeal5 = new Meal(USER_MEAL_5_ID, LocalDateTime.of(2024, Month.FEBRUARY, 16, 16, 44), "user meal5", 1200);
    public static final Meal userMeal6 = new Meal(USER_MEAL_6_ID, LocalDateTime.of(2024, Month.OCTOBER, 20, 0, 0), "user meal6", 800);
    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2024, Month.OCTOBER, 23, 20, 23), "admin meal1", 670);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "breakfast", 500);
    }

    public static Meal getUpdatedUserMeal() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(LocalDateTime.of(2023, Month.FEBRUARY, 20, 15, 15));
        updated.setDescription("updated description");
        updated.setCalories(3330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().usingDefaultComparator().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().usingDefaultComparator().isEqualTo(expected);
    }
}
