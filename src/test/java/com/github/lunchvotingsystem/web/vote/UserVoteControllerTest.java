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
import static com.github.lunchvotingsystem.web.user.UserTestData.USER_ID;
import static com.github.lunchvotingsystem.web.user.UserTestData.USER_MAIL;
import static com.github.lunchvotingsystem.web.vote.UserVoteController.REST_URL;
import static com.github.lunchvotingsystem.web.vote.UserVoteTestData.*;
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
        assertEquals(RESTAURANT_1_VOTE, returned);
        returned = JsonUtil.readValue(perform(MockMvcRequestBuilders.get(REST_URL_SLASH + TODAY.minusDays(1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString(), VoteTo.class);
        assertEquals(RESTAURANT_2_VOTE, returned);
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
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_3_VOTE)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(RESTAURANT_3_VOTE, service.findByDate(USER_ID, TODAY).get());
    }

    @Test
    void updateUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_3_VOTE)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createInvalid() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(INVALID_RESTAURANT_VOTE)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalidNotToday() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY.minusDays(1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_3_VOTE)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(ILLEGAL_VOTE_DAY_EXCEPTION)));
        assertEquals(RESTAURANT_2_VOTE, service.findByDate(USER_ID, TODAY.minusDays(1)).get());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createInvalidNotToday() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY.minusDays(1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_3_VOTE)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(ILLEGAL_VOTE_DAY_EXCEPTION)));
        assertEquals(RESTAURANT_1_VOTE, service.findByDate(USER_ID, TODAY).get());
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY.plusDays(1))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_3_VOTE)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(ILLEGAL_VOTE_DAY_EXCEPTION)));
        assertTrue(service.findByDate(USER_ID, TODAY.plusDays(1)).isEmpty());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateInvalidAfterDeadline() throws Exception {
        setFixedTodayClockAfterDeadline();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_3_VOTE)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(VOTE_CHANGE_AFTER_DEADLINE_EXCEPTION)));
        assertEquals(RESTAURANT_1_VOTE, service.findByDate(USER_ID, TODAY).get());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateNotFound() throws Exception {
        setFixedTodayClock();
        perform(MockMvcRequestBuilders.put(REST_URL_SLASH + TODAY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(INVALID_RESTAURANT_VOTE)))
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