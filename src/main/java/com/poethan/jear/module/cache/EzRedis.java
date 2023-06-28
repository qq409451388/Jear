package com.poethan.jear.module.cache;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Slf4j
@Component
public class EzRedis {
    private Jedis jedis;

    @Value("${redis.host:127.0.0.1}")
    private String host;

    @Value("${redis.port:6379}")
    private Integer port;

    @PostConstruct
    public void init(){
        this.jedis = new Jedis(host, port);
        this.jedis.connect();
    }

    public String set(String k, String v) {
        return jedis.set(k, v);
    }

    public String get(String k) {
        return jedis.get(k);
    }
}
