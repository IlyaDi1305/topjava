package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface CrudRepository {

    public List<Meal> getAll();

    public Meal getById(int id);

    public void add(Meal meal);

    public void update(int id, Meal meal);

    public void delete(int id);
}
