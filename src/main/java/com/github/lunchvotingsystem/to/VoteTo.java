package com.github.lunchvotingsystem.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class VoteTo {
    private LocalDate date;
    private int userId;
    private int restaurantId;
}
