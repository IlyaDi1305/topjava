package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.repository.MealsCrudRepository;
import ru.javawebinar.topjava.repository.MapMealsCrudRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private MealsCrudRepository crudRepository;

    @Override
    public void init() {
        crudRepository = new MapMealsCrudRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        log.info("Received GET request with action: {}", action);

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "new":
                log.info("Action 'new': Showing form to create a new meal");
                showNewForm(request, response);
                break;
            case "edit":
                log.info("Action 'edit': Showing form to edit a meal");
                showEditForm(request, response);
                break;
            case "delete":
                log.info("Action 'delete': Deleting a meal");
                delete(request, response);
                break;
            default:
                log.info("Action 'list': Listing all meals");
                listMeals(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Integer id = getId(request);
        log.info("Received POST request with id: {}", id);
        if (id == null) {
            add(request, response);
        } else if (id != null) {
            update(request, response);
        } else {
            listMeals(request, response);
        }
    }

    private void listMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Listing all meals");
        List<Meal> meals = crudRepository.getAll();
        request.setAttribute("meals", MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Showing form for a new meal");
        request.setAttribute("meal", MealsUtil.getDefaultMeal());
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = getId(request);
        log.info("Showing edit form for meal with id: {}", id);
        Meal existingMeal = crudRepository.getById(id);
        request.setAttribute("meal", existingMeal);
        request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Meal newMeal = parseFromRequest(request);
        log.info("Adding new meal: {}", newMeal);
        crudRepository.add(newMeal);
        response.sendRedirect("meals");
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Meal meal = parseFromRequest(request);
        log.info("Updating meal with id: {}, new details: {}", getId(request), meal);
        crudRepository.update(meal);
        response.sendRedirect("meals");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Deleting meal with id: {}", getId(request));
        crudRepository.delete(getId(request));
        response.sendRedirect("meals");
    }

    private static Integer getId(HttpServletRequest request) {
        try {
            return Integer.valueOf(request.getParameter("id"));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Meal parseFromRequest(HttpServletRequest request) {
        Integer id = getId(request);
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        return new Meal(id, dateTime, description, calories);
    }
}