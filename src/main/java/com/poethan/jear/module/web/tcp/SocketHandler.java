package com.poethan.jear.module.web.tcp;

import com.poethan.jear.utils.EzDataUtils;
import com.poethan.jear.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
abstract public class SocketHandler<T> extends ChannelInboundHandlerAdapter {
    public static final ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Class<T> msgDataType;

    protected void setMsgDataType(Class<T> msgDataType) {
        this.msgDataType = msgDataType;
    }

    /**
     * 读取到客户端发来的消息
     *
     * @param ctx ChannelHandlerContext
     * @param msg msg
     * @throws Exception e
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        T data = EzDataUtils.convertValue(msg, this.msgDataType);
        log.info("收到消息: " + new String((byte[]) msg));
        this.channelReadTrueType(ctx, data);
    }

    abstract public void channelReadTrueType(ChannelHandlerContext ctx, T msg);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("新的客户端连接：" + ctx.channel().id().asShortText());
        clients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端断开：" + ctx.channel().id().asShortText());
        clients.remove(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
        clients.remove(ctx.channel());
    }
}