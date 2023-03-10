package com.example.mytask.cache;

import com.example.mytask.cache.entity.CacheEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class CacheFactory<K, V> {
    public Cache<K, V> getCacheByConfiguration() {
        CacheEntity cacheEntity = uploadPropertiesFileAndGetCacheEntity();
        switch (cacheEntity.getCacheType()) {
            case "LRU": {
                return new LruCache<>(cacheEntity.getCacheSize());
            }
            default: {
                return new LfuCache<>(cacheEntity.getCacheSize());
            }
        }
    }

    private CacheEntity uploadPropertiesFileAndGetCacheEntity() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("application.yaml")).getFile());
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            return objectMapper.readValue(file, CacheEntity.class);
        } catch (IOException e) {
            log.error("Cannot find application properties file for configurate cache", e);
            throw new RuntimeException("Cannot find application properties file for configurate cache", e);
        }
    }
}
