package com.github.lunchvotingsystem.service;

import com.github.lunchvotingsystem.model.Dish;
import com.github.lunchvotingsystem.model.Restaurant;
import com.github.lunchvotingsystem.repository.DishRepository;
import com.github.lunchvotingsystem.repository.RestaurantRepository;
import com.github.lunchvotingsystem.to.MenuTo;
import com.github.lunchvotingsystem.util.MenusUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.github.lunchvotingsystem.util.ValidationUtil.checkExistedResource;
import static com.github.lunchvotingsystem.util.ValidationUtil.checkResourceModification;

@Service
@AllArgsConstructor
public class MenuService {
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    public Optional<MenuTo> findByDate(int restaurantId, LocalDate date) {
        checkExistedResource(restaurantRepository.existsById(restaurantId), restaurantId);
        List<Dish> dishes = dishRepository.getAllByRestaurantIdAndMenuDate(restaurantId, date);
        if (dishes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(MenusUtil.createMenu(dishes));
    }

    @Transactional
    public void deleteExisted(int restaurantId, LocalDate date) {
        checkExistedResource(restaurantRepository.existsById(restaurantId), restaurantId);
        checkResourceModification(dishRepository.deleteByRestaurantIdAndMenuDate(restaurantId, date), date);
    }

    @Transactional
    public void update(int restaurantId, LocalDate date, MenuTo menu) {
        Restaurant restaurant = checkExistedResource(restaurantRepository.findById(restaurantId), restaurantId);
        dishRepository.deleteByRestaurantIdAndMenuDate(restaurantId, date);
        dishRepository.saveAll(MenusUtil.prepareToSave(menu.getDishes(), date, restaurant));
    }
}
