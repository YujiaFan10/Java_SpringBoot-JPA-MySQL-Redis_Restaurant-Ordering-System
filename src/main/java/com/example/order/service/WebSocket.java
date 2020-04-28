package com.example.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this);
        log.info("[webSocket] new connection, total: {}", webSocketSet.size());
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        log.info("[webSocket] connection broken, total: {}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message){
        log.info("[webSocket] Received message from the client server: {}", message);
    }

    public void sendMessage(String message){
        for(WebSocket webSocket: webSocketSet){
            log.info("[webSocket] broadcast, message={}", message);
            try{
                webSocket.session.getBasicRemote().sendText(message); //get message
            } catch (Exception e){
                e.printStackTrace(); //print error
            }
        }
    }

}
