package com.github.lunchvotingsystem.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    public static final String USERS_CACHE = "users";
    public static final String RESTAURANT_MENU_CACHE = "restaurantMenu";
    public static final String VOTE_COUNTS_CACHE = "voteCounts";

    @Bean
    public CacheManager cacheManager(Ticker ticker) {
        List<Cache> caches = new ArrayList<>();

        caches.add(new CaffeineCache(USERS_CACHE,
                Caffeine.newBuilder()
                        .ticker(ticker)
                        .expireAfterAccess(1, TimeUnit.HOURS)
                        .maximumSize(10000)
                        .build()));

        caches.add(new CaffeineCache(RESTAURANT_MENU_CACHE,
                Caffeine.newBuilder()
                        .ticker(ticker)
                        .expireAfterAccess(1, TimeUnit.HOURS)
                        .maximumSize(100)
                        .build()));

        caches.add(new CaffeineCache(VOTE_COUNTS_CACHE,
                Caffeine.newBuilder()
                        .ticker(ticker)
                        .expireAfterWrite(30, TimeUnit.SECONDS)
                        .maximumSize(100)
                        .build()));
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public Ticker ticker() {
        return Ticker.systemTicker();
    }
}