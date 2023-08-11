package io.mynotes.mynotes.config;

import io.mynotes.api.management.model.Token;
import io.mynotes.mynotes.helper.PropertiesHandler;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {
    @Autowired
    PropertiesHandler properties;

    @Bean
    public CacheManager EhCacheManager() {
        org.ehcache.config.CacheConfiguration<String, Token> cacheConfig = CacheConfigurationBuilder
                .newCacheConfigurationBuilder(String.class, Token.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().offheap(1, MemoryUnit.MB)
                                .build())
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(properties.getCacheTtl())))
                .build();

        CachingProvider cachingProvider = Caching.getCachingProvider();
        CacheManager cacheManager = cachingProvider.getCacheManager();

        javax.cache.configuration.Configuration<String, Token> configuration = Eh107Configuration
                .fromEhcacheCacheConfiguration(cacheConfig);

        cacheManager.createCache("tokenCache", configuration);

        return cacheManager;
    }
}
