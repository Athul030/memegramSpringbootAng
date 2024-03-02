package com.athul.memegramspring.dto;

import com.athul.memegramspring.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@NoArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private String id;
    private String chatId;
    private int senderId;
    private MessageType messageType;
    private String content;
    private Instant time;
    private Boolean isRead;
    private String imageName;

}
