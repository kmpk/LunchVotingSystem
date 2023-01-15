package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.service.MenuService;
import com.github.lunchvotingsystem.to.MenuTo;
import com.github.lunchvotingsystem.util.ValidationUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.github.lunchvotingsystem.web.restaurant.AdminMenuController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminMenuController {
    static final String REST_URL = "/api/admin/restaurants";

    private final MenuService service;

    @GetMapping("/{id}/menus/{date}")
    public ResponseEntity<MenuTo> get(@PathVariable int id, @PathVariable LocalDate date) {
        log.info("get {} of {}", date, id);
        return ResponseEntity.of(service.findByDate(id, date));
    }

    @DeleteMapping("/{id}/menus/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restMenu", key = "#date")
    public void delete(@PathVariable int id, @PathVariable LocalDate date) {
        log.info("delete {} of {}", date, id);
        service.deleteExisted(id, date);
    }

    @PutMapping(value = "/{id}/menus/{date}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restMenu", key = "#date")
    public void update(@Valid @RequestBody MenuTo menu, @PathVariable int id, @PathVariable LocalDate date) {
        log.info("update {} with id={}", menu, id);
        ValidationUtil.assureDateConsistent(menu, date);
        service.update(id, menu);
    }
}
