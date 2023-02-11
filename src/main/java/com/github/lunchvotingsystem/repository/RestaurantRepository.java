package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.Restaurant;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query(value = """
            SELECT restaurant_id AS id, restaurant.name AS restaurantName, address,
             menu_date AS menuDate, d.id AS dishId, d.name AS dishName, cost
            FROM restaurant INNER JOIN dish d on restaurant.id = d.restaurant_id
            WHERE menu_date = :date
            """, nativeQuery = true)
    List<Tuple> getWithTodayDishes(LocalDate date);
}