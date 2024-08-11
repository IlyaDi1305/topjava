package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

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

        List<UserMealWithExcess> mealsTo = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        for (UserMeal meal : meals) {
            ((Map<LocalDate, Integer>) new HashMap<LocalDate, Integer>()).put(meal.getDateTime().toLocalDate(),
                    ((Map<LocalDate, Integer>) new HashMap<LocalDate, Integer>()).getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());
        }

        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                ((List<UserMealWithExcess>) new ArrayList<UserMealWithExcess>())
                        .add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                                ((Map<LocalDate, Integer>) new HashMap<LocalDate, Integer>()).get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return new ArrayList<>();
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), meals.stream()
                            .collect(Collectors.groupingBy(
                                    meal1 -> meal1.getDateTime().toLocalDate(),
                                    Collectors.summingInt(UserMeal::getCalories)))
                            .get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
                })
                .collect(Collectors.toList());
    }
}
