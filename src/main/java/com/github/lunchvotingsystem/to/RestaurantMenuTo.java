package com.github.lunchvotingsystem.to;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RestaurantMenuTo extends NamedTo {
    String address;
    LocalDate menuDate;
    List<DishTo> menu;

    public RestaurantMenuTo(Integer id, String name, String address, LocalDate menuDate, List<DishTo> menu) {
        super(id, name);
        this.address = address;
        this.menuDate = menuDate;
        this.menu = menu;
    }

    public void addDish(DishTo dish) {
        menu.add(dish);
    }
}
