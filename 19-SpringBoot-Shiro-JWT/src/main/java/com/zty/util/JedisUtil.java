package com.zty.util;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * redis工具类
 * 一些jedis操作方法
 */
@Component
public class JedisUtil {

    //通过jedis连接池获取redis连接
    @Resource
    private JedisPool jedisPool;

    //获取连接
    private Jedis getResource(){
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {

        try (Jedis jedis = getResource()){
            jedis.set(key,value);
            return value;
        }
    }

    public int expire(byte[] key, int i) {
        try (Jedis jedis = getResource()){
            jedis.expire(key,i);
            return i;
        }
    }

    public byte[] get(byte[] key) {
        try (Jedis jedis = getResource()){
            return jedis.get(key);
        }
    }

    public void del(byte[] key) {
        try (Jedis jedis = getResource()){
            jedis.del(key);
        }
    }

    public Set<byte[]> keys(String pattern) {
        try (Jedis jedis = getResource()){
            return jedis.keys((pattern + "*").getBytes());
        }
    }

    public Set<byte[]> values(String pattern) {
        try (Jedis jedis = getResource()){
            Set<String> keys = jedis.keys(pattern + "*");
            Set<byte[]> values = new HashSet<>();
            for (String key : keys) {
                values.add(jedis.get(key).getBytes());
            }
            return values;
        }
    }
}
