package com.athul.memegramspring.dto;

import com.athul.memegramspring.entity.Message;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {

    private String id;
    private List<Integer> participantUserIds;
    private List<MessageDTO> messages;
    private String lastMessageId;
}
