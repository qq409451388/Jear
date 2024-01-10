package com.poethan.jear.web.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Setter
@Slf4j
public class EzWebSocketServer {
    protected int port;
    protected Class<? extends EzWebSocketServerHandler> ezWebSocketHandlerClass;

    public EzWebSocketServer(int port, Class<? extends EzWebSocketServerHandler> ezWebSocketHandlerClass){
        this.setPort(port);
        this.setEzWebSocketHandlerClass(ezWebSocketHandlerClass);
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch)
                                throws Exception {
                            // http 的解码器
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("http-codec",
                                    new HttpServerCodec());
                            //  负责将 Http 的一些信息例如版本
                            // 和 Http 的内容继承一个 FullHttpRequesst
                            pipeline.addLast("aggregator",
                                    new HttpObjectAggregator(65536));
                            // 大文件写入的类
                            ch.pipeline().addLast("http-chunked",
                                    new ChunkedWriteHandler());
                            // websocket 处理类
                            pipeline.addLast("handler",
                                    ezWebSocketHandlerClass.getDeclaredConstructor().newInstance());
                        }
                    });
            // 监听端口
            Channel ch = b.bind(this.port).sync().channel();
            log.info("start ws://127.0.0.1:"+this.port);
            this.callAfterServerStart();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    protected void callAfterServerStart(){}
}
