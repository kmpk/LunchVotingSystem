package com.github.lunchvotingsystem;

import java.time.LocalDate;

public interface HasLocalDate {
    LocalDate getDate();

    void setDate(LocalDate date);

    default boolean isDateSet() {
        return getDate() != null;
    }
}
