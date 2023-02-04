package com.github.lunchvotingsystem.web.restaurant;


import com.github.lunchvotingsystem.model.Dish;
import com.github.lunchvotingsystem.to.MenuTo;
import com.github.lunchvotingsystem.util.MenusUtil;
import com.github.lunchvotingsystem.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "id", "restaurant", "menuDate");

    public static final LocalDate TODAY = LocalDate.now();
    public static Dish DISH_1_RESTAURANT_1 = new Dish(1, TODAY, "Restaurant 1 dish 1", 10000);
    public static Dish DISH_2_RESTAURANT_1 = new Dish(2, TODAY, "Restaurant 1 dish 2", 20000);
    public static Dish DISH_3_RESTAURANT_1 = new Dish(3, TODAY, "Restaurant 1 dish 3", 30000);
    public static Dish DISH_1_RESTAURANT_2 = new Dish(4, TODAY, "Restaurant 2 dish 1", 40000);
    public static Dish DISH_2_RESTAURANT_2 = new Dish(5, TODAY, "Restaurant 2 dish 2", 50000);
    public static Dish YESTERDAY_DISH_1_RESTAURANT_1 = new Dish(6, TODAY.minusDays(1), "Restaurant 1 yesterday dish 1", 70000);
    public static Dish YESTERDAY_DISH_2_RESTAURANT_1 = new Dish(7, TODAY.minusDays(1), "Restaurant 1 yesterday dish 2", 80000);

    public static MenuTo getFirstRestaurantMenu() {
        return MenusUtil.createMenu(List.of(DISH_1_RESTAURANT_1, DISH_2_RESTAURANT_1, DISH_3_RESTAURANT_1));
    }

    public static MenuTo getUpdatedMenu() {
        LocalDate menuDate = DISH_1_RESTAURANT_1.getMenuDate();
        return MenusUtil.createMenu(
                List.of(new Dish(null, menuDate, "new dish 1", 1111),
                        new Dish(null, menuDate, "new dish 2", 2222)));
    }

    public static MenuTo getInvalidMenu() {
        return MenusUtil.createMenu(
                List.of(new Dish(null, null, "", -1),
                        new Dish(null, null, "", 0)));
    }

    public static MenuTo getDuplicateMenu() {
        return MenusUtil.createMenu(
                List.of(new Dish(null, null, "duplicate", 1111),
                        new Dish(null, null, "duplicate", 2222)));
    }

    public static MenuTo getFirstRestaurantYesterdayMenu() {
        return MenusUtil.createMenu(List.of(YESTERDAY_DISH_1_RESTAURANT_1, YESTERDAY_DISH_2_RESTAURANT_1));
    }

    public static Iterable<Dish> allDishesWithoutFirstRestaurantTodayMenu() {
        return List.of(DISH_1_RESTAURANT_2, DISH_2_RESTAURANT_2, YESTERDAY_DISH_1_RESTAURANT_1, YESTERDAY_DISH_2_RESTAURANT_1);
    }
}