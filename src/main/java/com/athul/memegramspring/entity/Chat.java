package com.athul.memegramspring.entity;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "chats")
public class Chat {

    @Id
    private String id;
    private List<String> participantUsernames;
    private List<Message> messages;
    private String lastMessageId;


}
