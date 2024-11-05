package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = DATAJPA)
public class MealServiceWithDataJpaRepositoryImplTest extends AbstractMealServiceTest {

    @Test
    public void getMealWithUser() {
        Meal meal = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        USER_MATCHER.assertMatch(meal.getUser(), admin);
    }
}