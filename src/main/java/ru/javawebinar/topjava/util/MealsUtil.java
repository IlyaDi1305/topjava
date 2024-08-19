package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.concurrent.locks.Lock;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsUtil {

    private static final Logger log = getLogger(MealsUtil.class);
    public static List<Meal> meals = new CopyOnWriteArrayList<>();
    private final Lock lock = new ReentrantLock();
    private static int mealId = 1;

    public MealsUtil() {
        lock.lock();
        try {
            log.info("Initializing MealsUtil with sample data");
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
            meals.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
            meals.forEach(meal -> meal.setId(mealId++));
            log.info("Sample data initialization complete");
        } finally {
            lock.unlock();
        }
    }

    public List<Meal> getAll() {
        lock.lock();
        try {
            log.info("Retrieving all meals");
            return meals;
        } finally {
            lock.unlock();
        }
    }

    public Meal getById(int id) {
        log.info("Retrieving meal with id: {}", id);
        for (Meal meal : meals) {
            if (meal.getId() == id) {
                log.info("Meal found: {}",meal);
                return meal;
            }
        }
        log.warn("Meal not found for id: {}", id);
        return null;
    }

    public void add(Meal meal) {
        lock.lock();
        try {
            log.info("Adding meal: {}", meal);
            meal.setId(mealId++);
            meals.add(meal);
        } finally {
            lock.unlock();
        }
    }

    public void update(Meal meal) {
        lock.lock();
        try {
            log.info("Updating meal with id: {}", meal.getId());
            Meal existingMeal = getById(meal.getId());
            if (existingMeal != null) {
                existingMeal.setDateTime(meal.getDateTime());
                existingMeal.setDescription(meal.getDescription());
                existingMeal.setCalories(meal.getCalories());
            }
        } finally {
            lock.unlock();
        }
    }

    public void delete(int id) {
        log.info("Deleting meal with id: {}", id);
        boolean removed = meals.removeIf(meal -> meal.getId()==id);
        if (removed) {
            log.info("Meal deleted: {}", id);
        } else {
            log.warn("Meal not found for deletion: {}", id);
        }
    }

    public static final int CALORIES_PER_DAY = 2000;

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        log.info("Filtering meals by time range and calories");
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
                );

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > CALORIES_PER_DAY))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}