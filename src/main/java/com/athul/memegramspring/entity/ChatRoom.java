package com.athul.memegramspring.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "chats")
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {

    @Id
    private String id;
    private List<Integer> participantUserIds;
    private List<Message> messages;
    private String lastMessageId;


}
