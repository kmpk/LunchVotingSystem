package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    List<Dish> getAllByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);

    int deleteByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);
}
