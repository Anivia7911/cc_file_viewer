package com.viewer.trivial.cache;

import org.springframework.stereotype.Component;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Component
public class FileConvertCache {
    // 缓存原始文件路径 -> 转换后的 PDF 路径
    private final Cache<String, String> cache;

    public FileConvertCache() {
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS) // 缓存 1 小时后过期
                .maximumSize(100) // 最多缓存 100 个文件
                .build();
    }

    public String getIfPresent(String key) {
        return cache.getIfPresent(key);
    }

    public void put(String key, String value) {
        cache.put(key, value);
    }

    public void invalidate(String key) {
        cache.invalidate(key);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }
}
