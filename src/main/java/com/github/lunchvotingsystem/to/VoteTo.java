package com.github.lunchvotingsystem.to;

import com.github.lunchvotingsystem.HasLocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class VoteTo implements HasLocalDate {
    private LocalDate date;
    @NotNull
    private int restaurantId;
}
