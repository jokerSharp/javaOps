package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int FIRST_USER_MEAL_ID = START_SEQ + 3;
    public static final int SECOND_USER_MEAL_ID = START_SEQ + 4;
    public static final int THIRD_USER_MEAL_ID = START_SEQ + 5;
    public static final int FOURTH_USER_MEAL_ID = START_SEQ + 6;
    public static final int FIFTH_USER_MEAL_ID = START_SEQ + 7;
    public static final int ADMIN_MEAL_ID = START_SEQ + 8;
    public static final int MEAL_NOT_FOUND = 100;

    public static final Meal firstUserMeal = new Meal(FIRST_USER_MEAL_ID, LocalDateTime.of(2024, Month.MAY, 30, 23, 12), "meal1", 470);
    public static final Meal secondUserMeal = new Meal(SECOND_USER_MEAL_ID, LocalDateTime.of(2024, Month.MAY, 29, 6, 5), "meal2", 570);
    public static final Meal thirdUserMeal = new Meal(THIRD_USER_MEAL_ID, LocalDateTime.of(2024, Month.MAY, 28, 15, 16), "meal3", 300);
    public static final Meal fourthUserMeal = new Meal(FOURTH_USER_MEAL_ID, LocalDateTime.of(2024, Month.FEBRUARY, 16, 18, 24), "meal4", 274);
    public static final Meal fifthUserMeal = new Meal(FIFTH_USER_MEAL_ID, LocalDateTime.of(2024, Month.JANUARY, 6, 20, 44), "meal5", 936);
    public static final Meal adminMeal = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2023, Month.OCTOBER, 23, 20, 23), "meal6", 670);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "breakfast", 500);
    }

    public static Meal getUpdatedUserMeal() {
        Meal updated = new Meal(firstUserMeal);
        updated.setDateTime(LocalDateTime.of(2023, Month.FEBRUARY, 20, 15, 15));
        updated.setDescription("updated description");
        updated.setCalories(3330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
