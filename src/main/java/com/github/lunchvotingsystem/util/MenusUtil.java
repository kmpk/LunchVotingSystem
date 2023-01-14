package com.github.lunchvotingsystem.util;

import com.github.lunchvotingsystem.model.Dish;
import com.github.lunchvotingsystem.model.Restaurant;
import com.github.lunchvotingsystem.to.DishTo;
import com.github.lunchvotingsystem.to.MenuTo;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class MenusUtil {

    public static MenuTo createMenu(LocalDate date, Collection<Dish> dishes) {
        return new MenuTo(date, dishes.stream().map(MenusUtil::toFromDish).toList());
    }

    public static List<Dish> prepareToSave(List<DishTo> tos, LocalDate date, Restaurant restaurant) {
        return tos.stream().map(toToDishFunction(date)).map(dish -> {
            dish.setRestaurant(restaurant);
            return dish;
        }).toList();
    }

    public static List<Dish> dishFromTos(Collection<DishTo> tos, LocalDate date) {
        return tos.stream().map(toToDishFunction(date)).toList();
    }

    private static Function<DishTo, Dish> toToDishFunction(LocalDate date) {
        return dto -> new Dish(null, date, dto.getName(), dto.getCost());
    }

    public static DishTo toFromDish(Dish dish) {
        return new DishTo(dish.getName(), dish.getCost());
    }

    public static List<DishTo> tosFromDishes(Collection<Dish> dishes) {
        return dishes.stream().map(MenusUtil::toFromDish).toList();
    }
}
