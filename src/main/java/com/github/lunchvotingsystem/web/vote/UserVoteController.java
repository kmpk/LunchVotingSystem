package com.github.lunchvotingsystem.web.vote;

import com.github.lunchvotingsystem.service.VoteService;
import com.github.lunchvotingsystem.to.VoteTo;
import com.github.lunchvotingsystem.util.security.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserVoteController {
    static final String REST_URL = "/api/profile/votes";
    private final VoteService service;

    @GetMapping("/{date}")
    public ResponseEntity<VoteTo> get(@PathVariable LocalDate date, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get {} of {}", date, userId);
        return ResponseEntity.of(service.findByDate(userId, date));
    }

    @PutMapping(value = "/{date}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable LocalDate date, @RequestParam int restaurantId, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with {}", date, restaurantId);
        service.update(authUser.id(), date, restaurantId);
    }
}
