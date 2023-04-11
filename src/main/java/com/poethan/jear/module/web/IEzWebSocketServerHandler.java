package com.poethan.jear.module.web;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public interface IEzWebSocketServerHandler extends ChannelHandler {
    void callAfterChannelStart(ChannelHandlerContext ctx, FullHttpRequest req);

    void callAfterChannelClose(ChannelHandlerContext ctx, CloseWebSocketFrame frame);

    void callAfterMessageComeIn(ChannelHandlerContext ctx, TextWebSocketFrame frame);
}