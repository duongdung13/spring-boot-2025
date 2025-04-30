package com.dungcode.demo.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    /**
     * Configures a simple in-memory cache manager.
     * In a production environment, you might want to use a more robust solution
     * like Redis, Caffeine, or EhCache.
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("products");
    }
}
