package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.to.VoteCountTo;

import java.util.List;

import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT_2_ID;

public class RestaurantVotingTestData {
    public static VoteCountTo VOTING_COUNT_FIRST_RESTAURANT = new VoteCountTo(RESTAURANT_1_ID, 1);
    public static VoteCountTo VOTING_COUNT_SECOND_RESTAURANT = new VoteCountTo(RESTAURANT_2_ID, 1);

    public static List<VoteCountTo> getVoting() {
        return List.of(VOTING_COUNT_FIRST_RESTAURANT, VOTING_COUNT_SECOND_RESTAURANT);
    }

    public static VoteCountTo YESTERDAY_VOTING_COUNT_FIRST_RESTAURANT = new VoteCountTo(RESTAURANT_2_ID, 1);

    public static List<VoteCountTo> getYesterdayVoting() {
        return List.of(YESTERDAY_VOTING_COUNT_FIRST_RESTAURANT);
    }
}
