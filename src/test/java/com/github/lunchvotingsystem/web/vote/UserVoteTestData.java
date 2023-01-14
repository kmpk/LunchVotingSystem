package com.github.lunchvotingsystem.web.vote;

import com.github.lunchvotingsystem.to.VoteTo;

import static com.github.lunchvotingsystem.web.restaurant.MenuTestData.TODAY;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.RESTAURANT_2_ID;
import static com.github.lunchvotingsystem.web.user.UserTestData.USER_ID;

public class UserVoteTestData {
    public static VoteTo USER_VOTE = new VoteTo(TODAY, USER_ID, RESTAURANT_1_ID);
    public static VoteTo USER_YESTERDAY_VOTE = new VoteTo(TODAY.minusDays(1), USER_ID, RESTAURANT_2_ID);

    public static VoteTo getUpdated() {
        return new VoteTo(TODAY, USER_ID, RESTAURANT_2_ID);
    }
}
