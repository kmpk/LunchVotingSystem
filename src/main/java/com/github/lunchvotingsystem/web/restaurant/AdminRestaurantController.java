package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.model.Restaurant;
import com.github.lunchvotingsystem.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.github.lunchvotingsystem.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    private final RestaurantRepository repository;

    @GetMapping("/{id}")
    @Operation(summary = "get restaurant info")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get {}", id);
        return ResponseEntity.of(repository.findById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete restaurant")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = {"restMenu", "voteCounts"}, allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.deleteExisted(id);
    }

    @GetMapping
    @Operation(summary = "get all restaurants")
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "create new restaurant")
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "update restaurant info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restMenu", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        checkExisted(repository.existsById(id), id);
        repository.save(restaurant);
    }
}
