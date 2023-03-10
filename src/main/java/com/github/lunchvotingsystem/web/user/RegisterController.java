package com.github.lunchvotingsystem.web.user;

import com.github.lunchvotingsystem.exception.IllegalRequestDataException;
import com.github.lunchvotingsystem.model.User;
import com.github.lunchvotingsystem.to.UserTo;
import com.github.lunchvotingsystem.util.UsersUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.github.lunchvotingsystem.config.CacheConfig.USERS_CACHE;
import static com.github.lunchvotingsystem.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RegisterController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirements()
public class RegisterController extends AbstractUserController {
    static final String REST_URL = "/api/register";

    protected boolean setUserRole;

    @Autowired
    @SuppressWarnings("deprecation")
    public void setUserRoleFlag(Environment environment) {
        this.setUserRole = environment.acceptsProfiles("REGISTER_ROLE_SET");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "register new user")
    @ApiResponse(responseCode = "403", description = "Authentication is not allowed here", content = @Content(schema = @Schema(hidden = true)))
    @ApiResponse(responseCode = "422", content = @Content(schema = @Schema(hidden = true)))
    @CacheEvict(cacheNames = USERS_CACHE, key = "#userTo.email")
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) throws IllegalRequestDataException {
        log.info("register {}", userTo);
        checkNew(userTo);
        User created = prepareAndSave(setUserRole ? UsersUtil.createNewFromToWithUserRole(userTo) : UsersUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath().path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}