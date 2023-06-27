package com.poethan.jear.module.cache;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;

@Component
public class EzRedis {
    private RedisProperties.Jedis jedis;

}
