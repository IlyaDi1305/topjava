package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface CrudRepository {

    List<Meal> getAll();

    Meal getById(int id);

    void add(Meal meal);

    void update(int id, Meal meal);

    void delete(int id);
}
