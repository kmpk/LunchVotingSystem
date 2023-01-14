package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.Restaurant;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query(value = """
            SELECT RESTAURANT_ID AS id,RESTAURANT.NAME AS restaurantName, ADDRESS AS address,
             MENU_DATE AS menuDate, D.ID AS dishId, D.NAME AS dishName,D.COST AS cost
            FROM RESTAURANT INNER JOIN DISH D on RESTAURANT.ID = D.RESTAURANT_ID
            WHERE MENU_DATE = :date
            ORDER BY id
            """, nativeQuery = true)
    List<Tuple> getWithTodayDishes(LocalDate date);
}