package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.repository.RestaurantRepository;
import com.github.lunchvotingsystem.to.RestaurantMenuTo;
import com.github.lunchvotingsystem.util.RestaurantsUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.lunchvotingsystem.web.restaurant.RestaurantMenuController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantMenuController {
    static final String REST_URL = "/api/restaurants/menus";

    private final RestaurantRepository repository;

    @GetMapping("/{date}")
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "restMenu")
    public List<RestaurantMenuTo> get(@PathVariable LocalDate date) {
        log.info("get {}", date);
        return RestaurantsUtil.parseTuples(repository.getWithTodayDishes(date));
    }
}
