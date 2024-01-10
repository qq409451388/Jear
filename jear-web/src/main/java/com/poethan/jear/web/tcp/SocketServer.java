package com.poethan.jear.web.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Gjing
 **/
@Slf4j
@Component
public class SocketServer<E, D> {
    @Getter
    private ServerBootstrap serverBootstrap;

    @Setter
    @Getter
    private SocketInitializer<E, D> socketInitializer;

    /**
     * netty服务监听端口
     */
    @Value("${netty.port:8088}")
    private int port;
    /**
     * 主线程组数量
     */
    @Value("${netty.bossThread:1}")
    private int bossThread;

    /**
     * 启动netty服务器
     */
    public void start() {
        try {
            this.init();
            this.serverBootstrap.bind(this.port);
            log.info("Netty started on port: {} (TCP) with boss thread {}", this.port, this.bossThread);
        } catch (Exception e) {
            log.error("Netty start error", e);
        }
    }

    /**
     * 初始化netty配置
     */
    private void init() throws Exception {
        SocketChannel channel = new NioSocketChannel();
        this.socketInitializer = this.getSocketInitializer();
        socketInitializer.initChannel(channel);
        // 创建两个线程组，bossGroup为接收请求的线程组，一般1-2个就行
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(this.bossThread);
        // 实际工作的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(bossGroup, workerGroup) // 两个线程组加入进来
                .channel(NioServerSocketChannel.class)  // 配置为nio类型
                .childHandler(socketInitializer); // 加入自己的初始化器
    }

    public static class SocketInitializer<E, D> extends ChannelInitializer<SocketChannel> {
        private final Class<? extends MessageToMessageEncoder<E>> encoder;
        private final Class<? extends MessageToMessageDecoder<D>> decoder;
        private final Class<? extends SocketHandler<?>> socketHandler;

        public SocketInitializer(Class<? extends MessageToMessageEncoder<E>> encoder,
                                 Class<? extends MessageToMessageDecoder<D>> decoder,
                                 Class<? extends SocketHandler<?>> socketHandler) {
            this.encoder = encoder;
            this.decoder = decoder;
            this.socketHandler = socketHandler;
        }

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            pipeline.addLast(decoder.getDeclaredConstructor().newInstance());
            pipeline.addLast(encoder.getDeclaredConstructor().newInstance());
            pipeline.addLast(socketHandler.getDeclaredConstructor().newInstance());
        }
    }
}