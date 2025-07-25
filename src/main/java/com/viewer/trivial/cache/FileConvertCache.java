package com.viewer.trivial.cache;

import com.viewer.model.FileAttributeModel;
import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Component
public class FileConvertCache {

    private final Cache<String, FileAttributeModel> cache;

    public FileConvertCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    public FileAttributeModel getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, FileAttributeModel value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }
}
