package com.github.lunchvotingsystem.service;

import com.github.lunchvotingsystem.exception.IllegalRequestDataException;
import com.github.lunchvotingsystem.model.Dish;
import com.github.lunchvotingsystem.model.Restaurant;
import com.github.lunchvotingsystem.repository.DishRepository;
import com.github.lunchvotingsystem.repository.RestaurantRepository;
import com.github.lunchvotingsystem.to.MenuTo;
import com.github.lunchvotingsystem.util.MenusUtil;
import com.github.lunchvotingsystem.util.ValidationUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MenuService {
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    @Transactional(readOnly = true)
    public Optional<MenuTo> findByDate(int restaurantId, LocalDate date) {
        ValidationUtil.checkExisted(restaurantRepository.existsById(restaurantId), restaurantId);
        List<Dish> dishes = dishRepository.getAllByRestaurantIdAndMenuDate(restaurantId, date);
        if (dishes.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(MenusUtil.createMenu(date, dishes));
    }

    public void deleteExisted(int restaurantId, LocalDate date) {
        ValidationUtil.checkExisted(restaurantRepository.existsById(restaurantId), restaurantId);
        if (dishRepository.deleteByRestaurantIdAndMenuDate(restaurantId, date) == 0) {
            throw new IllegalRequestDataException("Menu with restaurant id=" + restaurantId + " and date " + date + " not found");
        }
    }

    public void update(int restaurantId, MenuTo menu) {
        Restaurant restaurant = ValidationUtil.checkExisted(restaurantRepository.getReferenceById(restaurantId), restaurantId);
        dishRepository.deleteByRestaurantIdAndMenuDate(restaurantId, menu.getDate());
        dishRepository.saveAll(MenusUtil.prepareToSave(menu.getDishes(), menu.getDate(), restaurant));
    }
}
