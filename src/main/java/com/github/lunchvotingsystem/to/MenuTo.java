package com.github.lunchvotingsystem.to;

import com.github.lunchvotingsystem.HasLocalDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MenuTo implements HasLocalDate {
    LocalDate date;
    @NotEmpty
    List<@Valid DishTo> dishes;
}
