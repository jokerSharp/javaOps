package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import java.time.Month;

import static java.time.LocalDateTime.of;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void createWithoutDate() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new Meal(null, null, "Созданный ужин", 300), USER_ID));
    }

    @Test
    public void createWithoutDescription() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new Meal(null, of(2020, Month.FEBRUARY, 1, 18, 0), null, 300), USER_ID));
    }
}