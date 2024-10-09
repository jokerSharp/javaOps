package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = getLogger(InMemoryMealRepository.class);
    private final List<Meal> meals;
    private final AtomicInteger nextId;

    {
        nextId = new AtomicInteger(1);
        meals = new CopyOnWriteArrayList<>();
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal add(Meal meal) {
        log.debug("Adding meal: {}", meal);
        int id = nextId.getAndIncrement();
        meal.setId(id);
        meals.add(meal);
        return meal;
    }

    public List<Meal> getAll() {
        log.debug("Getting meal list");
        return Collections.unmodifiableList(meals);
    }

    @Override
    public Meal getById(int id) {
        log.debug("Getting meal with id: {}", id);
        Meal dummy = new Meal(null, null, 0);
        dummy.setId(id);
        int result = Arrays.binarySearch(meals.toArray(new Meal[0]), dummy, Comparator.comparing(Meal::getId));
        if (result < 0) {
            return null;
        }
        return meals.get(result);
    }

    @Override
    public Meal update(Meal meal) {
        log.debug("Updating meal: {}", meal);
        for (int i = 0; i < meals.size(); i++) {
            if (meals.get(i).getId() == meal.getId()) {
                meals.set(i, meal);
                return meals.get(i);
            }
        }
        return null;
    }

    @Override
    public void delete(int id) {
        log.debug("Deleting meal with id: {}", id);
        meals.remove(getById(id));
    }
}
