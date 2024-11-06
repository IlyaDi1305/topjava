package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL_MEALS;


@Transactional
public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    private MealService mealService;

    @Test
    public void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MEALS + "/" + MEAL1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    public void createMeal() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL_MEALS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL_MEALS + "/" + MEAL1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_MEALS + "/" + MEAL1_ID))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.getAll(USER_ID), mealsWithoutMeal1);
    }

    @Test
    public void getAllMeals() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_MEALS))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meals));
    }

    @Test
    public void getBetween() throws Exception {
        String startDateTime = "2020-01-30T09:00:00";
        String endDateTime = "2020-01-30T23:59:59";
        perform(MockMvcRequestBuilders.get(REST_URL_MEALS + "/get-between" + "?startDateTime=" + startDateTime + "&endDateTime=" + endDateTime))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MEAL_MATCHER.contentJson(meal3, meal2, meal1));
    }
}