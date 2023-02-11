package com.github.lunchvotingsystem.web.vote;

import com.github.lunchvotingsystem.service.VoteService;
import com.github.lunchvotingsystem.to.VoteTo;
import com.github.lunchvotingsystem.util.security.AuthUser;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(value = UserVoteController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
public class UserVoteController {
    static final String REST_URL = "/api/profile/votes";
    private final VoteService service;

    @GetMapping("/{date}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "get user vote at provided date")
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(hidden = true)))
    public ResponseEntity<VoteTo> get(@PathVariable LocalDate date, @AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("get {} of {}", date, userId);
        return ResponseEntity.of(service.findByDate(userId, date));
    }

    @PutMapping(value = "/{date}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "voteCounts", key = "#date")
    @Operation(summary = "set user vote for restaurant at provided date")
    @ApiResponse(responseCode = "400", description = "Cannot vote not for today or change vote after deadline", content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(responseCode = "422", content = @Content(schema = @Schema(hidden = true)))
    public void update(@PathVariable LocalDate date, @RequestBody @Valid VoteTo vote, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with {}", date, vote.getRestaurantId());
        service.update(authUser.id(), vote, date);
    }
}
