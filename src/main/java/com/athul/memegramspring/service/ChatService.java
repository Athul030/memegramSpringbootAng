package com.athul.memegramspring.service;


import com.athul.memegramspring.dto.ChatRoomDTO;
import com.athul.memegramspring.dto.MessageDTO;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.ChatMessage;
import com.athul.memegramspring.entity.ChatRoom;
import com.athul.memegramspring.entity.Message;
import com.athul.memegramspring.entity.User;

import java.util.List;

public interface ChatService {

    ChatRoomDTO createChatRoom(ChatRoom chatRoom);

    MessageDTO saveMessage(Message message);

    List<MessageDTO> getChatHistory(String roomId);

    void updateUserPresence(int userId,boolean status);

    UserDTO getTheOtherUser(String roomId, int currentUserId);

}
