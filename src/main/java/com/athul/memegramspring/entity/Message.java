package com.athul.memegramspring.entity;

import com.athul.memegramspring.enums.MessageType;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String chatId;
    private String senderId;
    private MessageType messageType;
    private String content;

    private Instant time;

    private Boolean isRead;
    private String imageName;


}
