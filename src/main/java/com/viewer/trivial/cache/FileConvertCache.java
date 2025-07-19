package com.viewer.trivial.cache;

import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Component
public class FileConvertCache {

    private final Cache<String, String> cache;

    public FileConvertCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .maximumSize(100)
                .build();
    }

    public String getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        cache.invalidate(key);
    }

    public void removeByConvertedFilename(String filename) {
        // 遍历缓存，找到 value 包含 filename 的 key 删除
        cache.asMap().entrySet().removeIf(entry -> entry.getValue().contains(filename));
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }
}
