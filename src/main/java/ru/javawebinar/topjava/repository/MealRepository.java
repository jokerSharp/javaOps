package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {

    void addMeal(Meal meal);
    List<Meal> getMeals();
    Meal getMealById(int id);
    void updateMeal(Meal meal);
    void deleteMeal(Meal meal);
}
