package com.poethan.gear.module.web;

import javax.websocket.*;

@ClientEndpoint()
public class WebSocketClientHandler {
    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Client收到的消息: " + message);
    }

    @OnClose
    public void onClose() {}
}
