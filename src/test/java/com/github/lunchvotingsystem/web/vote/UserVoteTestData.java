package com.github.lunchvotingsystem.web.vote;

import com.github.lunchvotingsystem.to.VoteTo;

import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.*;

public class UserVoteTestData {
    public static VoteTo RESTAURANT_1_VOTE = new VoteTo(RESTAURANT_1_ID);
    public static VoteTo RESTAURANT_2_VOTE = new VoteTo(RESTAURANT_2_ID);
    public static VoteTo RESTAURANT_3_VOTE = new VoteTo(RESTAURANT_3_ID);
    public static VoteTo INVALID_RESTAURANT_VOTE = new VoteTo(NOT_FOUND);
}
