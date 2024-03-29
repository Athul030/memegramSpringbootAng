package com.athul.memegramspring.entity;

import com.athul.memegramspring.enums.MessageType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "messages")
@NoArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    private String id;

    private String chatId;
    private int senderId;
    private MessageType messageType;
    private String content;

    private Instant time;

    private Boolean isRead;
    private String imageName;


}
