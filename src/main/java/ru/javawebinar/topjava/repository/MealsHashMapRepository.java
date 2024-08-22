package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsHashMapRepository implements CrudMealsRepository {

    private static final Logger log = getLogger(MealsHashMapRepository.class);
    private final Lock lock = new ReentrantLock();
    private final Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public MealsHashMapRepository() {
        fillDefaultData();
    }

    @Override
    public List<Meal> getAll() {
        lock.lock();
        try {
            log.info("Retrieving all meals");
            return new ArrayList<>(meals.values());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Meal getById(Integer id) {
        Meal mealById = meals.get(id);
        log.info("Retrieving meal with id: {}", id);
        if (mealById != null) {
            log.info("Meal found: {}", mealById);
            return mealById;
        } else {
            log.warn("Meal not found for id: {}", id);
            return null;
        }
    }

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
        if (meals.get(id) != null) {
            log.info("Deleting meal with id: {}", id);
            meals.remove(id);
            log.info("Meal deleted: {}", id);
        } else {
            log.warn("Meal not found for deletion: {}", id);
        }
    }

    private void fillDefaultData() {
        Meal[] mealsDefault = {
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500)),
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000)),
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500)),
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100)),
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000)),
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500)),
                (new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410))};
        List<Meal> mealsSample = Arrays.asList(mealsDefault);
        for (Meal meal : mealsSample) {
            add(meal);
        }
        log.info("Sample data fill complete");
    }
}