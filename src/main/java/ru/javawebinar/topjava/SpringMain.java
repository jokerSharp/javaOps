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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            //MealRestController tests
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            List<MealTo> mealToList = mealRestController.getAll();
            mealToList.forEach(System.out::println);
            Meal firstUserMeal = new Meal(LocalDateTime.now(), "first user meal", 300);
            mealRestController.create(firstUserMeal);
            mealRestController.get(firstUserMeal.getId());
            Meal updateMealRequest = new Meal(firstUserMeal.getId(), LocalDateTime.now(), "updated first user meal", 400);
            updateMealRequest.setUserId(firstUserMeal.getUserId());
            mealRestController.update(updateMealRequest, firstUserMeal.getId());
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
