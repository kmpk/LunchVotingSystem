package com.github.lunchvotingsystem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public interface HasLocalDate {
    LocalDate getDate();

    void setDate(LocalDate date);

    @Schema(hidden = true)
    default boolean isDateSet() {
        return getDate() != null;
    }
}
