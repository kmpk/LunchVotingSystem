package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.repository.RestaurantRepository;
import com.github.lunchvotingsystem.to.RestaurantMenuTo;
import com.github.lunchvotingsystem.util.RestaurantsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.github.lunchvotingsystem.config.CacheConfig.RESTAURANT_MENU_CACHE;
import static com.github.lunchvotingsystem.web.restaurant.RestaurantMenuController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
public class RestaurantMenuController {
    static final String REST_URL = "/api/restaurants/menus";

    private final RestaurantRepository repository;

    @GetMapping("/{date}")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = RESTAURANT_MENU_CACHE)
    @Operation(summary = "get all restaurants with menu of the day for provided date")
    public List<RestaurantMenuTo> get(@PathVariable LocalDate date) {
        log.info("get {}", date);
        return RestaurantsUtil.parseTuples(repository.getWithTodayDishes(date));
    }
}
