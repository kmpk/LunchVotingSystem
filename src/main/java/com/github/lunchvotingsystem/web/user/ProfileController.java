package com.github.lunchvotingsystem.web.user;

import com.github.lunchvotingsystem.model.User;
import com.github.lunchvotingsystem.to.UserTo;
import com.github.lunchvotingsystem.util.UsersUtil;
import com.github.lunchvotingsystem.util.security.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static com.github.lunchvotingsystem.config.CacheConfig.USERS_CACHE;
import static com.github.lunchvotingsystem.util.ValidationUtil.assureIdConsistent;

@RestController
@RequestMapping(value = ProfileController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@ApiResponse(responseCode = "401", content = @Content(schema = @Schema(hidden = true)))
public class ProfileController extends AbstractUserController {
    static final String REST_URL = "/api/profile";

    @GetMapping
    @Operation(summary = "get logged user info")
    @ResponseStatus(HttpStatus.OK)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser);
        return authUser.getUser();
    }

    @DeleteMapping
    @Operation(summary = "delete logged user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = USERS_CACHE, allEntries = true)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        checkModificationRestriction(authUser.id());
        super.delete(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "update logged user info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = USERS_CACHE, key = "#userTo.email")
    @ApiResponse(responseCode = "422", content = @Content(schema = @Schema(hidden = true)))
    public void update(@RequestBody @Valid UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        checkModificationRestriction(authUser.id());
        User user = authUser.getUser();
        prepareAndSave(UsersUtil.updateFromTo(user, userTo));
    }
}