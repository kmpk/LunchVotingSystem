package com.github.lunchvotingsystem.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class VoteCountTo {
    int restaurantId;
    int votes;
}
