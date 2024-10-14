package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    ConfigurableApplicationContext appCtx;
    private MealRestController controller;

    @Override
    public void init(ServletConfig config) {
        appCtx = (ConfigurableApplicationContext) config.getServletContext().getAttribute("configurableApplicationContext");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        log.debug("Parsed meal ID: {}", id);
        int userId = SecurityUtil.authUserId();

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        meal.setUserId(userId);

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, Integer.valueOf(id));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDate dateFrom;
        LocalDate dateTo;
        LocalTime timeFrom;
        LocalTime timeTo;
        try {
            dateFrom = LocalDate.parse(request.getParameter("dateFrom"));
        } catch (Exception e) {
            dateFrom = LocalDate.MIN;
        }
        try {
            dateTo = LocalDate.parse(request.getParameter("dateTo"));
        } catch (Exception e) {
            dateTo = LocalDate.MAX;
        }
        try {
            timeFrom = LocalTime.parse(request.getParameter("timeFrom"));
        } catch (Exception e) {
            timeFrom = LocalTime.MIN;
        }
        try {
            timeTo = LocalTime.parse(request.getParameter("timeTo"));
        } catch (Exception e) {
            timeTo = LocalTime.MAX;
        }

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            SecurityUtil.setAuthUserId(userId);
        } catch (NumberFormatException ignore) {
        }

        String action = request.getParameter("action");
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.getAllFiltered(dateFrom, dateTo, timeFrom, timeTo));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
