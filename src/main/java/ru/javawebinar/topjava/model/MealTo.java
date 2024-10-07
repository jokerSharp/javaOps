package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealTo {
    private Integer mealToId;

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    private boolean isDeleted;

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess, Integer mealId, boolean isDeleted) {
        this.mealToId = mealId;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.isDeleted = isDeleted;
    }

    public Integer getMealToId() {
        return mealToId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
