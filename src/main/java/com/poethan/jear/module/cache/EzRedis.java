package com.poethan.jear.module.cache;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.Duration;

@Slf4j
@Component
public class EzRedis {
    private Jedis jedis;
    @Value("${redis.host:127.0.0.1}")
    private String host;

    @Value("${redis.port:6379}")
    private Integer port;

    @Value("${redis.retry:3}")
    private Integer reTryTimes;

    @Value("${redis.minIdle:3}")
    private Integer minIdle;

    @Value("${redis.maxIdle:3}")
    private Integer maxIdle;

    @Value("${redis.maxTotal:3}")
    private Integer maxTotal;

    @PostConstruct
    public void init(){
        GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWait(Duration.ofMillis(1000));
        JedisPool jedisPool = new JedisPool(poolConfig, host, port);
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }

        }
    }

    public String set(String k, String v) {
        return jedis.set(k, v);
    }

    public String get(String k) {
        return jedis.get(k);
    }

    public String setEx(String k, String v, long expire) {
        return jedis.setex(k, expire, v);
    }

    public boolean del(String k) {
        return 1 == jedis.del(k);
    }
}
