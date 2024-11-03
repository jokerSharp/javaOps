package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Util;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getReferenceById(userId));
        if (meal.isNew()) {
            return crudRepository.save(meal);
        }
        return get(meal.id(), userId) == null ? null : crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findById(id)
                .filter(meal -> meal.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAll().stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .toList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.findAll().stream()
                .filter(meal -> meal.getUser().getId() == userId)
                .filter(meal -> Util.isBetweenHalfOpen(meal.getDateTime(), startDateTime, endDateTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .toList();
    }

//    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filter) {
//        var meals = userRepository.getReferenceById(userId);
//        return meals == null ? Collections.emptyList() :
//                meals.getCollection().stream()
//                        .filter(filter)
//                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
//                        .toList();
//    }
}
