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

import java.util.List;

@Controller
public class MealRestController {
    private final MealService service;
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    public MealRestController(MealService service) {
        log.info("Initializing MealRestController with service {}", service.getClass().getSimpleName());
        this.service = service;
    }

    public Meal get(int id) {
        log.info("delete {}", id);
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
        log.info("creating meal: {}", meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void update(Meal meal) {
        log.info("updating meal: {}", meal);
        service.update(meal, SecurityUtil.authUserId());
    }
}