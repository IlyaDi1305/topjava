package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealsCrudRepository implements MealsCrudRepository {

    private static final Logger log = getLogger(InMemoryMealsCrudRepository.class);
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealsCrudRepository() {
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
        int id = meal.getId();
        boolean isUpdated = meals.replace(id, meal) != null;
        if (isUpdated) {
            log.info("Update meal : {}", meal);
            return meal;
        } else {
            log.info("No meal found with id: {}", id);
            return null;
        }
    }

    @Override
    public void delete(int id) {
        log.info("Meal deleted: {}", id);
        meals.remove(id);
    }
}