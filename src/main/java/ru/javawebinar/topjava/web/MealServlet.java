package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;
import static ru.javawebinar.topjava.util.TimeUtil.parseDateTimeFormat;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final String REDIRECT_LIST_MEAL = "/topjava/meals";
    private MealRepository mealRepository;

    @Override
    public void init() {
        mealRepository = new InMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("forwarding to meals");
        String forward = "";
        String action = request.getParameter("action");
        log.debug("parsed action: {}", action);
        if ("edit".equals(action)) {
            forward = INSERT_OR_EDIT;
            int id = Integer.parseInt(request.getParameter("mealId"));
            log.debug("parsed id: {}", id);
            Meal meal = mealRepository.getById(id);
            log.debug("setting meal in update request: {}", meal);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher(forward).forward(request, response);
        } else if ("delete".equals(action)) {
            forward = REDIRECT_LIST_MEAL;
            int id = Integer.parseInt(request.getParameter("mealId"));
            mealRepository.delete(id);
            request.setAttribute("mealsToFilteredList",
                    filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            response.sendRedirect(forward);

        } else {
            forward = LIST_MEAL;
            request.setAttribute("mealsToFilteredList",
                    filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            request.getRequestDispatcher(forward).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("forwarding to meal creation form");
        request.setCharacterEncoding("UTF-8");
        Meal meal = parseMealFromRequest(request);
        int mealId;
        try {
            mealId = Integer.parseInt(request.getParameter("mealId"));
            meal.setId(mealId);
            mealRepository.update(meal);
        } catch (NumberFormatException ignore) {
            mealRepository.add(meal);
        }
        request.setAttribute("mealsToFilteredList",
                filteredByStreams(mealRepository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        response.sendRedirect(REDIRECT_LIST_MEAL);
    }

    private Meal parseMealFromRequest(HttpServletRequest request) {
        LocalDateTime dateTime = parseDateTimeFormat(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(dateTime, description, calories);
    }
}
