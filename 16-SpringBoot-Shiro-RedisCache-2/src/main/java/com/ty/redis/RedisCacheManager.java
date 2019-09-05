package com.ty.redis;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

import javax.annotation.Resource;

public class RedisCacheManager<K,V> implements CacheManager {
    @Resource
    private RedisCache<K,V> redisCache;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return (Cache<K, V>) redisCache;
    }
}