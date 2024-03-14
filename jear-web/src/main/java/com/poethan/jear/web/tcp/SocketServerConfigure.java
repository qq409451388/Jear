package com.poethan.jear.web.tcp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jear.socket.server")
public class SocketServerConfigure {
    /**
     * netty服务监听端口
     */
    private Integer port;

    /**
     * 主线程组数量
     */
    private Integer bossThreadCount;
}
