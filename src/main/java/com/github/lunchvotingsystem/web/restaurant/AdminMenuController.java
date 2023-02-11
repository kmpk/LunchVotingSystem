package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.service.MenuService;
import com.github.lunchvotingsystem.to.MenuTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.github.lunchvotingsystem.config.CacheConfig.RESTAURANT_MENU_CACHE;
import static com.github.lunchvotingsystem.web.restaurant.AdminMenuController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
@ApiResponse(responseCode = "403", content = @Content(schema = @Schema(hidden = true)))
public class AdminMenuController {
    static final String REST_URL = "/api/admin/restaurants";

    private final MenuService service;

    @GetMapping("/{id}/menus/{date}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get restaurant menu of the day for provided date")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<MenuTo> get(@PathVariable int id, @PathVariable LocalDate date) {
        log.info("get {} of {}", date, id);
        return ResponseEntity.of(service.findByDate(id, date));
    }

    @DeleteMapping("/{id}/menus/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = RESTAURANT_MENU_CACHE, key = "#date")
    @Operation(summary = "delete restaurant menu of the day for provided date")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    public void delete(@PathVariable int id, @PathVariable LocalDate date) {
        log.info("delete {} of {}", date, id);
        service.deleteExisted(id, date);
    }

    @PutMapping(value = "/{id}/menus/{date}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = RESTAURANT_MENU_CACHE, key = "#date")
    @Operation(summary = "create or update restaurant menu of the day for provided date")
    @ApiResponse(responseCode = "422", content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    public void update(@Valid @RequestBody MenuTo menu, @PathVariable int id, @PathVariable LocalDate date) {
        log.info("update {} with id={}", menu, id);
        service.update(id, date, menu);
    }
}
