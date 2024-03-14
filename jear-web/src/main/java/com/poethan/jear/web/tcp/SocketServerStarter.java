package com.poethan.jear.web.tcp;

import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public abstract class SocketServerStarter<E, D, T> implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        SocketServer<E, D> socketServer = new SocketServer<>(getConfigure());
        SocketServer.SocketInitializer<E, D> initializer =
                new SocketServer.SocketInitializer<>(getEncoderClazz(), getDecoderClazz(), getSocketServerHandlerClazz());
        socketServer.setSocketInitializer(initializer);
        socketServer.start();
    }

    public abstract SocketServerConfigure getConfigure();

    public abstract Class<? extends MessageToMessageEncoder<E>> getEncoderClazz();
    public abstract Class<? extends MessageToMessageDecoder<D>> getDecoderClazz();
    public abstract Class<? extends SocketHandler<T>> getSocketServerHandlerClazz();
}
