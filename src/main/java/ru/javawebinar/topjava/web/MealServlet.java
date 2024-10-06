package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.InMemoryMealRepository;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    private final MealRepository mealRepository = new InMemoryMealRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forwarding to meals");

        request.setAttribute("mealsToFilteredList",
                filteredByStreams(mealRepository.getMeals(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
