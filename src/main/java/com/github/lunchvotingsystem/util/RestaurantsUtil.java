package com.github.lunchvotingsystem.util;

import com.github.lunchvotingsystem.to.DishTo;
import com.github.lunchvotingsystem.to.RestaurantMenuTo;
import jakarta.persistence.Tuple;
import lombok.experimental.UtilityClass;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class RestaurantsUtil {
    public static List<RestaurantMenuTo> parseTuples(List<Tuple> tuples) {
        Map<Integer, RestaurantMenuTo> tos = new LinkedHashMap<>();
        for (Tuple t : tuples) {
            Integer id = t.get("id", Integer.class);
            String restaurantName = t.get("restaurantname", String.class);
            String address = t.get("address", String.class);
            LocalDate menuDate = t.get("menudate", Date.class).toLocalDate();
            RestaurantMenuTo restaurant = tos.computeIfAbsent(id, i -> new RestaurantMenuTo(i, restaurantName, address, menuDate, new ArrayList<>()));
            restaurant.addDish(new DishTo(t.get("dishname", String.class), t.get("cost", Long.class)));
        }
        return List.copyOf(tos.values());
    }
}
