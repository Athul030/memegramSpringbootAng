package com.athul.memegramspring.controller;

import com.athul.memegramspring.entity.ChatMessage;
import com.athul.memegramspring.entity.Message;
import com.athul.memegramspring.entity.Notifications;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SocketController {

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ChatMessage chat(@DestinationVariable String roomId, ChatMessage message){
        System.out.println(message);
        return new ChatMessage(message.getMessage(),message.getUser());
    }
}
