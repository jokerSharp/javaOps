package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
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
        return MealsUtil.getTos(service.getAll(userId), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllFiltered(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        int userId = authUserId();
        log.info("filtered getAll for user with id {}", userId);
        LocalDate dateFromInController = dateFrom == null ? LocalDate.MIN : dateFrom;
        LocalDate dateToInController = dateTo == null ? LocalDate.MAX : dateTo;
        LocalTime timeFromInController = timeFrom == null ? LocalTime.MIN : timeFrom;
        LocalTime timeToInController = timeTo == null ? LocalTime.MAX : timeTo;
        return MealsUtil.getFilteredTos(service.getAllFiltered(userId, dateFromInController, dateToInController),
                authUserCaloriesPerDay(), timeFromInController, timeToInController);
    }

    public Meal get(int id) {
        int userId = authUserId();
        log.info("get {} for user with id {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        log.info("create {} for user with id {}", meal, userId);
        checkNew(meal);
        return service.create(meal, userId);
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