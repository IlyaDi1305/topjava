package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

public class MealsRepository implements CrudRepository {

    protected Map<Integer, Meal> meals = new HashMap<>();
    protected List<Meal> mealsTest = new ArrayList<Meal>();
    protected static int mealId = 1;

    public MealsRepository() {
        lock.lock();
        try {
            log.info("Initializing MealsUtil with sample data");
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
            mealsTest.add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
            for (Meal meal : mealsTest) {
                meal.setId(mealId++);
                meals.put(meal.getId(), meal);
            }
            log.info("Sample data initialization complete");
        } finally {
            lock.unlock();
        }
    }

    protected static final Logger log = getLogger(MealsUtil.class);
    protected final Lock lock = new ReentrantLock();

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
    public Meal getById(int id) {
        log.info("Retrieving meal with id: {}", id);
        if (meals.get(id) != null) {
            log.info("Meal found: {}", meals.get(id));
            return meals.get(id);
        } else {
            log.warn("Meal not found for id: {}", id);
            return null;
        }
    }

    @Override
    public void add(Meal meal) {
        lock.lock();
        try {
            log.info("Adding meal: {}", meal);
            meal.setId(mealId++);
            meals.put(meal.getId(), meal);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int id, Meal meal) {
        lock.lock();
        try {
            log.info("Update meal : {}", meal);
            meals.put(meal.getId(), meal);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void delete(int id) {
        if (meals.get(id) != null) {
            log.info("Deleting meal with id: {}", id);
            meals.remove(id);
            log.info("Meal deleted: {}", id);
        } else {
            log.warn("Meal not found for deletion: {}", id);
        }
    }
}