package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.to.RestaurantMenuTo;
import com.github.lunchvotingsystem.util.JsonUtil;
import com.github.lunchvotingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.lunchvotingsystem.web.restaurant.MenuTestData.TODAY;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantMenuController.REST_URL;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantMenuTestData.getTos;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantMenuTestData.getYesterdayTos;
import static com.github.lunchvotingsystem.web.user.UserTestData.USER_MAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantMenuControllerTest extends AbstractControllerTest {
    private static final String REST_URL_SLASH = REST_URL + "/";

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        List<RestaurantMenuTo> returned = JsonUtil.readValues(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), RestaurantMenuTo.class);
        assertEquals(getTos(), returned);
        returned = JsonUtil.readValues(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.minusDays(1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), RestaurantMenuTo.class);
        assertEquals(getYesterdayTos(), returned);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getEmpty() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.plusDays(1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.minusDays(2)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY))
                .andExpect(status().isUnauthorized());
    }
}