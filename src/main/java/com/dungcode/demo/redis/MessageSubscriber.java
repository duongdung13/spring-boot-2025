package com.dungcode.demo.redis;

import com.dungcode.demo.web_socket.NotificationWebSocketHandler;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class MessageSubscriber implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String payload = message.toString();
        System.out.println("Redis message received: " + payload);

        NotificationWebSocketHandler.sendMessageToAll(payload);
    }

}