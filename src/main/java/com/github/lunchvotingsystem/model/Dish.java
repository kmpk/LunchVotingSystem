package com.github.lunchvotingsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Entity
@Table(name = "dish",
        uniqueConstraints = @UniqueConstraint(name = Dish.DISH_NAME_MENU_DATE_RESTAURANT_CONSTRAINT,
                columnNames = {"name", "menu_date", "restaurant_id"}),
        indexes = @Index(name = "ix_dish_restaurant_menu_date", columnList = "restaurant_id, menu_date"))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {
    public static final String DISH_NAME_MENU_DATE_RESTAURANT_CONSTRAINT = "uk_dish_name_menu_date_restaurant";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    private Restaurant restaurant;

    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @Column(name = "cost", nullable = false)
    @Range(min = 1)
    @NotNull
    private long cost;

    public Dish(Integer id, LocalDate menuDate, String name, long cost) {
        super(id, name);
        this.menuDate = menuDate;
        this.cost = cost;
    }
}