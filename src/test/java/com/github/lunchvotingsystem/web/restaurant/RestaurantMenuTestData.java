package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.to.RestaurantMenuTo;
import com.github.lunchvotingsystem.util.MenusUtil;

import java.util.List;

import static com.github.lunchvotingsystem.web.restaurant.MenuTestData.*;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.restaurant1;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantTestData.restaurant2;

public class RestaurantMenuTestData {
    public static RestaurantMenuTo RESTAURANT_1_TO = new RestaurantMenuTo(
            restaurant1.getId(),
            restaurant1.getName(),
            restaurant1.getAddress(),
            DISH_1_RESTAURANT_1.getMenuDate(),
            MenusUtil.tosFromDishes(List.of(DISH_1_RESTAURANT_1, DISH_2_RESTAURANT_1, DISH_3_RESTAURANT_1)));
    public static RestaurantMenuTo RESTAURANT_2_TO = new RestaurantMenuTo(
            restaurant2.getId(),
            restaurant2.getName(),
            restaurant2.getAddress(),
            DISH_1_RESTAURANT_2.getMenuDate(),
            MenusUtil.tosFromDishes(List.of(DISH_1_RESTAURANT_2, DISH_2_RESTAURANT_2)));

    public static List<RestaurantMenuTo> getTos() {
        return List.of(RESTAURANT_1_TO, RESTAURANT_2_TO);
    }

    public static RestaurantMenuTo YESTERDAY_RESTAURANT_TO = new RestaurantMenuTo(
            restaurant1.getId(),
            restaurant1.getName(),
            restaurant1.getAddress(),
            YESTERDAY_DISH_1_RESTAURANT_1.getMenuDate(),
            MenusUtil.tosFromDishes(List.of(YESTERDAY_DISH_1_RESTAURANT_1, YESTERDAY_DISH_2_RESTAURANT_1)));

    public static List<RestaurantMenuTo> getYesterdayTos() {
        return List.of(YESTERDAY_RESTAURANT_TO);
    }
}
