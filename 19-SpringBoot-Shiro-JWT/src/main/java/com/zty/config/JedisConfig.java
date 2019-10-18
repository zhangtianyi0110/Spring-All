package com.zty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:properties/redis.properties")
public class JedisConfig {

    @Value("${spring.redis.database}")
    private String database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.jedis.pool.maxActive}")
    private Integer maxActive;
    @Value("${spring.redis.jedis.pool.maxWait}")
    private Integer maxWait;
    @Value("${spring.redis.jedis.pool.maxIdle}")
    private Integer maxIdle;
    @Value("${spring.redis.jedis.pool.minIdle}")
    private Integer minIdle;

    /**
     * 配置JedisPool
     * @return
     */
    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxActive);
        config.setMaxWaitMillis(maxWait);
        config.setMinIdle(minIdle);
        JedisPool jedisPool = new JedisPool(config,host,port,timeout);
        return jedisPool;
    }
}
