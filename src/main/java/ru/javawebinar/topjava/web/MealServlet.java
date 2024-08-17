package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private final MealsUtil mealsUtil = new MealsUtil();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        log.info("Received GET request with action: " + action);

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
                deleteMeal(request, response);
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
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        log.info("Received POST request with action: " + action);

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "add":
                addMeal(request, response);
                break;
            case "update":
                updateMeal(request, response);
                break;
            default:
                listMeals(request, response);
                break;
        }
    }

    private void listMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Listing all meals");
        List<Meal> meals = mealsUtil.getAllMeals();
        request.setAttribute("meals", MealsUtil.filteredByStreams(MealsUtil.meals, LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Showing form for a new meal");
        request.getRequestDispatcher("/meal-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = UUID.fromString(request.getParameter("uuid"));
        log.info("Showing edit form for meal with UUID: " + uuid);
        Meal existingMeal = mealsUtil.getMealById(uuid);
        request.setAttribute("meal", existingMeal);
        request.getRequestDispatcher("/meal-form.jsp").forward(request, response);
    }

    private void addMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        log.info("Adding new meal with description: " + description + ", dateTime: " + dateTime + ", calories: " + calories);
        Meal newMeal = new Meal(dateTime, description, calories);
        mealsUtil.addMeal(newMeal);
        response.sendRedirect("meals");
    }

    private void updateMeal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UUID uuid = UUID.fromString(request.getParameter("uuid"));
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        log.info("Updating meal with UUID: " + uuid + ", new description: " + description + ", new dateTime: " + dateTime + ", new calories: " + calories);
        Meal meal = new Meal(dateTime, description, calories);
        meal.setCalories(calories);
        meal.setUuid(uuid);
        mealsUtil.updateMeal(meal);
        response.sendRedirect("meals");
    }

    private void deleteMeal(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UUID uuid = UUID.fromString(request.getParameter("uuid"));
        log.info("Deleting meal with UUID: " + uuid);
        mealsUtil.deleteMeal(uuid);
        response.sendRedirect("meals");
    }
}

