package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void createWithoutName() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new User(null, null, "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER))));
    }

    @Test
    public void createWithoutEmail() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new User(null, "New", null, "newPass", 1555, false, new Date(), Collections.singleton(Role.USER))));
    }

    @Test
    public void createWithoutPassword() {
        assertThrows(DataIntegrityViolationException.class, () ->
                service.create(new User(null, "New", "new@gmail.com", null, 1555, false, new Date(), Collections.singleton(Role.USER))));
    }

}