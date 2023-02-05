package com.github.lunchvotingsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lunchvotingsystem.util.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant", uniqueConstraints = @UniqueConstraint(name = Restaurant.RESTAURANT_NAME_ADDRESS_CONSTRAINT, columnNames = {"name", "address"}))
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {
    public static final String RESTAURANT_NAME_ADDRESS_CONSTRAINT = "uk_restaurant_name_address";

    @NotBlank
    @Size(min = 2, max = 128)
    @Column(name = "address", nullable = false)
    @NoHtml
    private String address;

    @OneToMany(mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE) //https://stackoverflow.com/a/44988100/548473
    @JsonIgnore
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.address);
    }
}