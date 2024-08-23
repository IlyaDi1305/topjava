package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

public class MapMealsCrudRepository implements MealsCrudRepository {

    private static final Logger log = getLogger(MapMealsCrudRepository.class);
    private final Lock lock = new ReentrantLock();
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public MapMealsCrudRepository() {
        fillDefaultData();
    }

    private void fillDefaultData() {
        Meal[] defaultMeals = {
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)};
        for (Meal meal : defaultMeals) {
            add(meal);
        }
        log.info("Sample data fill complete");
    }

    @Override
    public List<Meal> getAll() {
        log.info("Retrieving all meals");
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(int id) {
        Meal mealById = meals.get(id);
        log.info("Retrieving meal with id: {}", id);
        if (mealById != null) {
            log.info("Meal found: {}", mealById);
            return mealById;
        } else {
            log.info("Meal not found for id: {}", id);
            return null;
        }
    }

    @Override
    public Meal add(Meal meal) {
        log.info("Adding meal: {}", meal);
        meal.setId(counter.incrementAndGet());
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal) {
        log.info("Update meal : {}", meal);
        meals.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(Integer id) {
        log.info("Meal deleted: {}", meals.remove(id));
    }
}