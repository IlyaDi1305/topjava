package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        // Мапа для отслеживания общих калорий на каждый день
        Map<LocalDate, Integer> dailyCaloriesMap = new HashMap<>();
        List<UserMealWithExcess> result = new ArrayList<>();

        // Считаем общие калории за день
        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            dailyCaloriesMap.put(date, dailyCaloriesMap.getOrDefault(date, 0) + meal.getCalories());
        }

        // Фильтруем приемы пищи по времени и проверяем на превышение
        for (UserMeal meal : meals) {
            LocalTime mealTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetweenHalfOpen(mealTime, startTime, endTime)) {
                LocalDate date = meal.getDateTime().toLocalDate();
                boolean excess = dailyCaloriesMap.get(date) > caloriesPerDay;
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }
        }
        return result;
    }
}
