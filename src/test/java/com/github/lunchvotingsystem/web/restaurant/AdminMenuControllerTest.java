package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.model.Dish;
import com.github.lunchvotingsystem.repository.DishRepository;
import com.github.lunchvotingsystem.to.MenuTo;
import com.github.lunchvotingsystem.util.JsonUtil;
import com.github.lunchvotingsystem.util.MenusUtil;
import com.github.lunchvotingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.github.lunchvotingsystem.model.Dish.DISH_DUPLICATE_NAME_EXCEPTION;
import static com.github.lunchvotingsystem.util.validation.NoHtml.EXCEPTION_NO_HTML;
import static com.github.lunchvotingsystem.web.restaurant.AdminMenuController.REST_URL;
import static com.github.lunchvotingsystem.web.restaurant.MenuTestData.*;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.*;
import static com.github.lunchvotingsystem.web.user.UserTestData.ADMIN_MAIL;
import static com.github.lunchvotingsystem.web.user.UserTestData.USER_MAIL;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL_SLASH = REST_URL + "/";
    public static final String SUB_PATH = "/menus/";

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        MenuTo returned = JsonUtil.readValue(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), MenuTo.class);
        assertEquals(getFirstRestaurantMenu(), returned);
        returned = JsonUtil.readValue(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY.minusDays(1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), MenuTo.class);
        assertEquals(getFirstRestaurantYesterdayMenu(), returned);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_3_ID + SUB_PATH + TODAY))
                .andDo(print())
                .andExpect(status().isNotFound());
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY.plusDays(1)))
                .andDo(print())
                .andExpect(status().isNotFound());
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY.minusDays(2)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertTrue(dishRepository.getAllByRestaurantIdAndMenuDate(RESTAURANT_1_ID, TODAY).isEmpty());
        DISH_MATCHER.assertMatch(dishRepository.findAll(), allDishesWithoutFirstRestaurantTodayMenu());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT_3_ID + SUB_PATH + TODAY))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY.plusDays(1)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        perform(MockMvcRequestBuilders.delete(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY.minusDays(2)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuTo updatedMenu = getUpdatedMenu();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenu)))
                .andDo(print())
                .andExpect(status().isNoContent());

        List<Dish> expected = MenusUtil.dishFromTos(updatedMenu.getDishes(), updatedMenu.getDate());
        DISH_MATCHER.assertMatch(dishRepository.getAllByRestaurantIdAndMenuDate(RESTAURANT_1_ID, TODAY), expected);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateNotFound() throws Exception {
        MenuTo updatedMenu = getUpdatedMenu();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + NOT_FOUND + SUB_PATH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateInvalid() throws Exception {
        MenuTo invalidMenu = getInvalidMenu();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalidMenu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateHtmlUnsafe() throws Exception {
        MenuTo updatedMenu = getUpdatedMenu();
        updatedMenu.getDishes().get(0).setName("<script>alert(123)</script>");
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(EXCEPTION_NO_HTML)));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicateName() throws Exception {
        MenuTo duplicateMenu = getDuplicateMenu();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + RESTAURANT_1_ID + SUB_PATH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicateMenu)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(DISH_DUPLICATE_NAME_EXCEPTION)));
    }
}