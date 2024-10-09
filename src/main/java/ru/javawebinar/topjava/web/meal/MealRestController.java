package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        log.info("Initializing MealRestController with service {}", service.getClass().getSimpleName());
        this.service = service;
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("deleting meal: {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getting all meals");
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        log.info("creating meal: {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        log.info("updating meal: {}, id: {}", meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getting all filtered meals");
        List<Meal> filteredMeals = service.getAllFiltered(SecurityUtil.authUserId(), startDate, endDate, startTime, endTime);
        return MealsUtil.getFilteredTos(filteredMeals, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}