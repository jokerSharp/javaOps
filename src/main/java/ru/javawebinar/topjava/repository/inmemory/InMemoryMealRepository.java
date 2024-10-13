package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal.getUserId() == userId ? meal : null;
        }
        // handle case: update, but not present in storage
        Meal updatedMeal = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        return meal.getUserId() == userId ? updatedMeal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Optional<Meal> optionalMeal = Optional.ofNullable(repository.get(id));
        return optionalMeal
                .map(meal -> repository.remove(id) != null)
                .orElse(false);
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> optionalMeal = Optional.ofNullable(repository.get(id));
        return optionalMeal
                .filter(meal -> meal.getUserId() == userId)
                .orElse(null);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(m -> m.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

