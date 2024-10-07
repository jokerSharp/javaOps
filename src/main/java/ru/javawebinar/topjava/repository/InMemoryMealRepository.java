package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {

    private List<Meal> meals;
    private static AtomicInteger nextId = new AtomicInteger(1);

    {
        meals = new CopyOnWriteArrayList<>();
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void addMeal(Meal meal) {
        meal.setId(nextId.getAndIncrement());
        meals.add(meal);
    }

    public List<Meal> getMeals() {
        return meals;
    }

    @Override
    public Meal getMealById(int id) {
        return meals.get(id - 1);
    }

    @Override
    public void updateMeal(Meal meal) {
        for (int i = 0; i < meals.size(); i++) {
            if (Objects.equals(meals.get(i).getId(), meal.getId())) {
                meals.set(i, meal);
                return;
            }
        }
    }

    @Override
    public void deleteMeal(Meal meal) {
        int id = meals.indexOf(meal);
        meal.setIsDeleted(true);
        Class clazz = meal.getClass();
        try {
            Field caloriesField = clazz.getDeclaredField("calories");
            caloriesField.setAccessible(true);
            caloriesField.set(meal, 0);
            meals.set(id, meal);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
