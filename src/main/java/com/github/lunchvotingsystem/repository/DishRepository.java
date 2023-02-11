package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish, Integer> {

    List<Dish> getAllByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);

    @Modifying
    @Query("DELETE FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.menuDate=:menuDate")
    int deleteByRestaurantIdAndMenuDate(int restaurantId, LocalDate menuDate);
}
