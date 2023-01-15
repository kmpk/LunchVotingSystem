package com.github.lunchvotingsystem.web.vote;

import com.github.lunchvotingsystem.service.VoteService;
import com.github.lunchvotingsystem.to.VoteTo;
import com.github.lunchvotingsystem.util.JsonUtil;
import com.github.lunchvotingsystem.web.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static com.github.lunchvotingsystem.exception.IllegalVoteDayException.ILLEGAL_VOTE_DAY_EXCEPTION;
import static com.github.lunchvotingsystem.exception.VoteChangeAfterDeadlineException.VOTE_CHANGE_AFTER_DEADLINE_EXCEPTION;
import static com.github.lunchvotingsystem.service.VoteService.CHANGE_VOTE_TIME_RESTRICTION;
import static com.github.lunchvotingsystem.web.restaurant.MenuTestData.TODAY;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT_2_ID;
import static com.github.lunchvotingsystem.web.user.UserTestData.*;
import static com.github.lunchvotingsystem.web.vote.UserVoteController.REST_URL;
import static com.github.lunchvotingsystem.web.vote.UserVoteTestData.USER_VOTE;
import static com.github.lunchvotingsystem.web.vote.UserVoteTestData.USER_YESTERDAY_VOTE;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserVoteControllerTest extends AbstractControllerTest {
    private final static String REST_URL_SLASH = REST_URL + "/";

    @Autowired
    private VoteService service;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        setFixedTodayClock();
        VoteTo returned = JsonUtil.readValue(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), VoteTo.class);
        assertEquals(USER_VOTE, returned);
        returned = JsonUtil.readValue(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.minusDays(1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), VoteTo.class);
        assertEquals(USER_YESTERDAY_VOTE, returned);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getNotFound() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.plusDays(1)))
                .andDo(print())
                .andExpect(status().isNotFound());
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.minusDays(2)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void update() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .param("restaurantId", RESTAURANT_2_ID + ""))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(UserVoteTestData.getUpdated(), service.findByDate(USER_ID, TODAY).get());
    }

    @Test
    void updateUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .param("restaurantId", RESTAURANT_1_ID + ""))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalidNotToday() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY.minusDays(1))
                .param("restaurantId", RESTAURANT_1_ID + ""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(ILLEGAL_VOTE_DAY_EXCEPTION)));
        assertEquals(USER_YESTERDAY_VOTE, service.findByDate(USER_ID, TODAY.minusDays(1)).get());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createInvalidNotToday() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY.minusDays(1))
                .param("restaurantId", RESTAURANT_1_ID + ""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(ILLEGAL_VOTE_DAY_EXCEPTION)));
        assertTrue(service.findByDate(ADMIN_ID, TODAY.minusDays(1)).isEmpty());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalidAfterDeadline() throws Exception {
        setFixedTodayClockAfterDeadline();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .param("restaurantId", RESTAURANT_1_ID + ""))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(VOTE_CHANGE_AFTER_DEADLINE_EXCEPTION)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalid() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .param("restaurantId", NOT_FOUND + ""))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    private void setFixedTodayClock() {
        service.setClock(Clock.fixed(TODAY.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()));
    }

    private void setFixedTodayClockAfterDeadline() {
        Instant instant = TODAY.atTime(CHANGE_VOTE_TIME_RESTRICTION.plus(1, ChronoUnit.SECONDS))
                .atZone(ZoneId.systemDefault())
                .toInstant();
        service.setClock(Clock.fixed(instant, ZoneId.systemDefault()));
    }
}