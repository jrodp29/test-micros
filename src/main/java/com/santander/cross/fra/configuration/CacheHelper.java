package com.santander.cross.fra.configuration;

import javax.ws.rs.ext.Provider;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.concurrent.TimeUnit;

@Provider
public class CacheHelper {

    private final Cache<String, String> FRATokenCache;

    public CacheHelper() {
        FRATokenCache = Caffeine.newBuilder()
                .expireAfterWrite(
                        ConfigProvider.getConfig().getValue("application.security.cache.fra-token-cache.time-to-live-seconds", Long.class),
                        TimeUnit.SECONDS)
                .maximumSize(
                        ConfigProvider.getConfig().getValue("application.security.cache.fra-token-cache.max-entries", Long.class)
                )
                .build();
    }

    public Cache<String, String> getFRATokenCache() {
        return FRATokenCache;
    }
}
