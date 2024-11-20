package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    protected UserService service;

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    protected JpaUtil jpaUtil;

    @Before
    public void setup() {
        cacheManager.getCache("users").clear();
        if (!isJdbc()) {
            jpaUtil.clear2ndLevelHibernateCache();
        }
    }

    @Test
    public void create() {
        User created = service.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(service.get(newId), newUser);
    }

    @Test
    public void createWithoutRole() {
        User newUser = getNew();
        newUser.getRoles().clear();
        User created = service.create(newUser);
        int newId = created.id();
        User expected = getNew();
        expected.getRoles().clear();
        expected.setId(newId);
        USER_MATCHER.assertMatch(created, expected);
        USER_MATCHER.assertMatch(service.get(newId), expected);
    }

    @Test
    public void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.USER)));
    }

    @Test
    public void delete() {
        service.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() {
        User user = service.get(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void getByEmail() {
        User user = service.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    public void update() {
        User updated = getUpdated();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), getUpdated());
    }

    @Test
    public void updateDeleteRole() {
        User updated = getUpdated();
        updated.getRoles().clear();
        service.update(updated);
        USER_MATCHER.assertMatch(service.get(USER_ID), updated);
    }

    @Test
    public void updateAddFirstRole() {
        User updated = new User(guest);
        updated.setRoles(Collections.singletonList(Role.USER));
        service.update(updated);
        User expected = new User(guest);
        expected.setRoles(Collections.singletonList(Role.USER));
        USER_MATCHER.assertMatch(service.get(GUEST_ID), expected);
    }

    @Test
    public void updateWithoutRole() {
        User updated = new User(guest);
        updated.setCaloriesPerDay(1234);
        service.update(updated);
        User expected = new User(guest);
        expected.setCaloriesPerDay(1234);
        USER_MATCHER.assertMatch(service.get(GUEST_ID), expected);
    }

    @Test
    public void updateAddSecondRole() {
        User updated = getUpdated();
        updated.getRoles().add(Role.USER);
        service.update(updated);
        User expected = getUpdated();
        expected.getRoles().add(Role.USER);
        USER_MATCHER.assertMatch(service.get(USER_ID), expected);
    }

    @Test
    public void updateRemoveRole() {
        User updated = new User(admin);
        updated.setRoles(Collections.singletonList(Role.USER));
        service.update(updated);
        User expected = new User(admin);
        expected.setRoles(Collections.singletonList(Role.USER));
        USER_MATCHER.assertMatch(service.get(ADMIN_ID), expected);
    }

    @Test
    public void getAll() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }

    @Test
    public void createWithException() throws Exception {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "  ", "mail@yandex.ru", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "  ", "password", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "  ", Role.USER)));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 9, true, new Date(), Set.of())));
        validateRootCause(ConstraintViolationException.class, () -> service.create(new User(null, "User", "mail@yandex.ru", "password", 10001, true, new Date(), Set.of())));
    }
}