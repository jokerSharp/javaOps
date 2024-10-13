package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        int userId = authUserId();
        log.info("getAll for user with id {}", userId);
        return MealsUtil.getTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public MealTo get(int id) {
        int userId = authUserId();
        log.info("get {} for user with id {}", id, userId);
        return MealsUtil.getTos(Arrays.asList(service.get(id, userId)), MealsUtil.DEFAULT_CALORIES_PER_DAY).stream().findFirst().get();
    }

    public MealTo create(Meal meal) {
        int userId = authUserId();
        log.info("create {} for user with id {}", meal, userId);
        checkNew(meal);
        return MealsUtil.getTos(Arrays.asList(service.create(meal, userId)), MealsUtil.DEFAULT_CALORIES_PER_DAY).stream().findFirst().get();
    }

    public void delete(int id) {
        int userId = authUserId();
        log.info("delete meal with id {} for user with id {}", id, userId);
        service.delete(id, authUserId());
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        log.info("update meal with id {} for user with id {}", id, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

}