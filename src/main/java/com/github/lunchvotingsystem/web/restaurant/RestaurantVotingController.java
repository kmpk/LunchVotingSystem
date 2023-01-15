package com.github.lunchvotingsystem.web.restaurant;

import com.github.lunchvotingsystem.repository.VoteRepository;
import com.github.lunchvotingsystem.to.VoteCountTo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.github.lunchvotingsystem.web.restaurant.RestaurantVotingController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantVotingController {
    static final String REST_URL = "/api/restaurants/votes";

    private VoteRepository repository;

    @GetMapping("/{date}")
    public List<VoteCountTo> get(@PathVariable LocalDate date) {
        log.info("get {}", date);
        return repository.countVotesByDate(date).stream()
                .map(vcp -> new VoteCountTo(vcp.getRestaurantId(), vcp.getCount()))
                .toList();
    }
}
