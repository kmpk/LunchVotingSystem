package com.github.lunchvotingsystem.util;

import com.github.lunchvotingsystem.model.Vote;
import com.github.lunchvotingsystem.to.VoteTo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VotesUtil {
    public static VoteTo getToFromVote(Vote vote) {
        return new VoteTo(vote.getRestaurant().getId());
    }
}
