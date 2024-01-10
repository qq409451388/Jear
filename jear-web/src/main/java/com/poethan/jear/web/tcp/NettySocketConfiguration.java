package com.poethan.jear.web.tcp;

import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class NettySocketConfiguration {

    @Bean
    public Map<String, Object> getScoketConfig() {
        Map<String, Object> mapConfig = new HashMap<>();
        mapConfig.put("DECODER", new ByteArrayDecoder());
        mapConfig.put("ENCODER", new ByteArrayEncoder());
        return mapConfig;
    }
}
