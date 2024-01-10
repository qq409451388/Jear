package com.poethan.jear.web.websocket;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;

public class WebSocketClient {
    private Session session;

    public void connect(String ip, int port) {
        String uri = "ws://"+ip+":"+port;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try {
            URI r = URI.create(uri);
            this.session = container.connectToServer(WebSocketClientHandler.class, r);
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }

    }

    public void send(String message){
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
