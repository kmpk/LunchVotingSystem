package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.model.Restaurant;
import com.github.lunchvotingsystem.web.MatcherFactory;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "dishes");

    public static final int RESTAURANT_1_ID = 100;
    public static final int RESTAURANT_2_ID = 101;
    public static final int RESTAURANT_3_ID = 102;
    public static final int NOT_FOUND = 110;

    public static Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, "First restaurant", "First restaurant address");
    public static Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, "Second restaurant", "Second restaurant address");
    public static Restaurant restaurant3 = new Restaurant(RESTAURANT_3_ID, "Third restaurant", "Third restaurant address");

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant", "New restaurant address");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_1_ID, "Updated Restaurant", "Updated restaurant address");
    }
}
