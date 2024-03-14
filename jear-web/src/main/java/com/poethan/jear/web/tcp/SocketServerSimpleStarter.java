package com.poethan.jear.web.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public abstract class SocketServerSimpleStarter extends SocketServerStarter<byte[], ByteBuf, byte[]> {

    @Override
    public Class<? extends MessageToMessageEncoder<byte[]>> getEncoderClazz() {
        return ByteArrayEncoder.class;
    }

    @Override
    public Class<? extends MessageToMessageDecoder<ByteBuf>> getDecoderClazz() {
        return ByteArrayDecoder.class;
    }

}
