package com.github.lunchvotingsystem.web.vote;

import com.github.lunchvotingsystem.to.VoteTo;

import static com.github.lunchvotingsystem.web.restaurant.MenuTestData.TODAY;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.*;

public class UserVoteTestData {
    public static VoteTo USER_VOTE = new VoteTo(TODAY, RESTAURANT_1_ID);
    public static VoteTo USER_YESTERDAY_VOTE = new VoteTo(TODAY.minusDays(1), RESTAURANT_2_ID);

    public static VoteTo getUpdatedVote() {
        return new VoteTo(TODAY, RESTAURANT_2_ID);
    }

    public static VoteTo getUpdatedYesterdayVote() {
        return new VoteTo(TODAY.minusDays(1), RESTAURANT_2_ID);
    }

    public static VoteTo getNotFound() {
        return new VoteTo(TODAY, NOT_FOUND);
    }

}
