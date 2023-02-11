package com.github.lunchvotingsystem.repository;

import com.github.lunchvotingsystem.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.github.lunchvotingsystem.config.CacheConfig.USERS_CACHE;

public interface UserRepository extends BaseRepository<User> {

    @Cacheable(cacheNames = USERS_CACHE, key = "#email")
    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);
}