package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //MealRestController tests
            setAuthUserId(1);
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            List<MealTo> mealToList = mealRestController.getAll();
            mealToList.forEach(System.out::println);
            Meal firstUserMeal = new Meal(LocalDateTime.now(), "first user meal", 300);
            mealRestController.create(firstUserMeal);
            mealRestController.get(firstUserMeal.getId());
            Meal updateMealRequest = new Meal(firstUserMeal.getId(), LocalDateTime.now(), "updated first user meal", 400);
            updateMealRequest.setUserId(firstUserMeal.getUserId());
            mealRestController.update(updateMealRequest, firstUserMeal.getId());
            List<MealTo> filteredByDateMealToList = mealRestController.getAllFiltered(
                    LocalDate.of(2020, Month.JANUARY, 31),
                    LocalDate.of(2024, Month.OCTOBER, 15),
                    null,
                    null);
            System.out.println("Filtered by date meals");
            filteredByDateMealToList.forEach(System.out::println);
            List<MealTo> filteredByTimeMealToList = mealRestController.getAllFiltered(
                    null,
                    null,
                    LocalTime.of(13, 0),
                    LocalTime.of(21, 0));
            System.out.println("Filtered by time meals");
            filteredByTimeMealToList.forEach(System.out::println);
            try {
                Meal negativeUpdateMealRequest = new Meal(firstUserMeal.getId() + 1, LocalDateTime.now(), "inconsistent id", 500);
                negativeUpdateMealRequest.setUserId(firstUserMeal.getUserId());
                mealRestController.update(negativeUpdateMealRequest, firstUserMeal.getId());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            try {
                mealRestController.update(new Meal(firstUserMeal.getId(), LocalDateTime.now(), "inconsistent id", 500), firstUserMeal.getId() + 1);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            mealRestController.get(firstUserMeal.getId());
            mealRestController.delete(firstUserMeal.getId());
            try {
                mealRestController.get(firstUserMeal.getId());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
