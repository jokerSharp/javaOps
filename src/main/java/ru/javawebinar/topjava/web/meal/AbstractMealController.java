package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

public abstract class AbstractMealController {

    protected static final Logger log = LoggerFactory.getLogger(AbstractMealController.class);

    @Autowired
    protected MealService service;

    @GetMapping()
    public String getAllFiltered(Model model,
                                 @Nullable @RequestParam(value = "startDate", required = false) String startDate,
                                 @Nullable @RequestParam(value = "startTime", required = false) String startTime,
                                 @Nullable @RequestParam(value = "endDate", required = false) String endDate,
                                 @Nullable @RequestParam(value = "endTime", required = false) String endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);
        List<Meal> mealsDateFiltered = service
                .getBetweenInclusive(parseLocalDate(startDate), parseLocalDate(endDate), userId);
        model.addAttribute("meals", MealsUtil.getFilteredTos(mealsDateFiltered,
                SecurityUtil.authUserCaloriesPerDay(), parseLocalTime(startTime), parseLocalTime(endTime)));
        return "meals";
    }

    @PostMapping()
    public String addMeal(HttpServletRequest request) {
        log.info("Adding meal");
        Integer mealId = getId(request);
        Meal meal = new Meal(
                mealId,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (mealId != null) {
            service.update(meal, SecurityUtil.authUserId());
        } else {
            service.create(meal, SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @DeleteMapping()
    public String deleteMeal(@RequestParam("id") Integer id) {
        log.info("Deleting meal");
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/form")
    public String addMeal(@RequestParam(value = "id", required = false) Integer id, Model model) {
        log.info("Edit meal form");
        if (id == null) {
            model.addAttribute("meal",
                    new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        } else {
            Meal meal = service.get(id, SecurityUtil.authUserId());
            model.addAttribute("meal", meal);
        }
        return "mealForm";
    }

    protected Integer getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return StringUtils.hasLength(request.getParameter("id")) ? Integer.parseInt(paramId) : null;
    }
}
