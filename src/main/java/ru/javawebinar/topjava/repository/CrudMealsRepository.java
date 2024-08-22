package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface CrudMealsRepository {

    List<Meal> getAll();

    Meal getById(Integer id);

    Meal add(Meal meal);

    Meal update(Meal meal);

    void delete(Integer id);
}
