package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenClosed;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        Meal meal = new Meal(LocalDateTime.of(2024, Month.OCTOBER, 16, 21, 0), "sixteen meal", 16);
        save(meal, 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("Saving meal: {} for user with id {}", meal, userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } else if (meal.getUserId() == userId) {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("Deleting meal with id {} for user with id{}", id, userId);
        Meal meal = repository.get(id);
        return meal.getUserId() == userId && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("Getting meal with id {} for user with id{}", id, userId);
        Optional<Meal> optionalMeal = Optional.ofNullable(repository.get(id));
        return optionalMeal
                .filter(meal -> meal.getUserId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("Getting all meals for user with id{}", userId);
        return filterByPredicate(userId, meal -> true);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDate dateFrom, LocalDate dateTo) {
        log.info("Getting filtered meals for user with id{}", userId);
        return filterByPredicate(userId, meal -> isBetweenClosed(meal.getDate(), dateFrom, dateTo));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

