package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/repository-spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"),
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_1_ID, USER_ID);
        assertMatch(meal, MealTestData.userMeal1);
    }

    @Test
    public void getOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_1_ID, ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_1_ID, USER_ID));
    }

    @Test
    public void deletedOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID_NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate fromDate = LocalDate.of(2024, Month.MAY, 28);
        LocalDate toDate = LocalDate.of(2024, Month.MAY, 30);
        List<Meal> meals = service.getBetweenInclusive(fromDate, toDate, USER_ID);
        assertMatch(meals, userMeal1, userMeal2, userMeal3);
    }

    @Test
    public void getBetweenInclusiveSingleDay() {
        LocalDate fromDate = LocalDate.of(2024, Month.MAY, 29);
        List<Meal> meals = service.getBetweenInclusive(fromDate, fromDate, USER_ID);
        assertMatch(meals, userMeal2);
    }

    @Test
    public void getBetweenInclusiveFromFutureDate() {
        LocalDate fromDate = LocalDate.of(3000, 1, 1);
        List<Meal> meals = service.getBetweenInclusive(fromDate, null, USER_ID);
        assertMatch(meals);
    }

    @Test
    public void getBetweenInclusiveToPastDate() {
        LocalDate toDate = LocalDate.of(1, 1, 1);
        List<Meal> meals = service.getBetweenInclusive(null, toDate, USER_ID);
        assertMatch(meals);
    }

    @Test
    public void getBetweenInclusiveWithNulls() {
        List<Meal> meals = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(meals, userMeal6, userMeal1, userMeal2, userMeal3, userMeal4, userMeal5);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, userMeal6, userMeal1, userMeal2, userMeal3, userMeal4, userMeal5);
    }

    @Test
    public void update() {
        Meal updated = getUpdatedUserMeal();
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_MEAL_1_ID, USER_ID), MealTestData.getUpdatedUserMeal());
    }

    @Test
    public void updateOtherUserMeal() {
        Meal updated = getUpdatedUserMeal();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updated = getNew();
        updated.setId(MEAL_ID_NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(MealTestData.getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createDuplicateDateTime() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(userMeal1.getDateTime(), "duplicate meal", 470), USER_ID));
    }
}