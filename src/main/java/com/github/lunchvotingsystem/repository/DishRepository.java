package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


public interface DishRepository extends JpaRepository<Dish, Integer> {

    List<Dish> getAllByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);

    @Transactional
    int deleteByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);
}
