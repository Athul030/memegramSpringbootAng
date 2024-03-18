package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.ChatRoomDTO;
import com.athul.memegramspring.dto.MessageDTO;
import com.athul.memegramspring.dto.UserDTO;
import com.athul.memegramspring.entity.ChatMessage;
import com.athul.memegramspring.entity.ChatRoom;
import com.athul.memegramspring.entity.Message;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.MessageType;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.ChatRoomRepo;
import com.athul.memegramspring.repository.MessageRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.ChatService;
import com.athul.memegramspring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepo chatRoomRepo;
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final UserService userService;

    //create or retrieve if existing
    @Override
    public ChatRoomDTO createChatRoom(ChatRoom chatRoom) {
        String errorCode = "ChatServiceImpl: createChatRoom";
        List<Integer> participantUserIds = chatRoom.getParticipantUserIds();
        Collections.sort(participantUserIds);
        ChatRoom existingChatRoom = chatRoomRepo.findByParticipantUserIds(participantUserIds);
        ModelMapper modelMapper = new ModelMapper();
        if (existingChatRoom !=null){
            existingChatRoom.setMessages(messageRepo.findByChatIdOrderByTimeDesc(existingChatRoom.getId()).orElseThrow(()->new ResourceNotFoundException("Chat Room","chat room Id",existingChatRoom.getId(),errorCode)));
            ChatRoomDTO chatRoomDTO = modelMapper.map(existingChatRoom,ChatRoomDTO.class);
            if(existingChatRoom.getMessages()!=null) {
                    chatRoomDTO.setMessages(existingChatRoom.getMessages().stream().map(x -> modelMapper.map(x, MessageDTO.class)).collect(Collectors.toList()));
            }
            return chatRoomDTO;
        }else{
            ChatRoom chatRoom1 = chatRoomRepo.save(chatRoom);
            ChatRoomDTO chatRoomDTO = modelMapper.map(chatRoom1, ChatRoomDTO.class);
            return chatRoomDTO;
        }
    }

    @Override
    public MessageDTO saveMessage(Message message) {
        message.setTime(Instant.now());
        message.setIsRead(false);
        Message message1 =messageRepo.save(message);
        ModelMapper modelMapper = new ModelMapper();
        //changed below from message to message1 , check it if error comes
        MessageDTO messageDTO = modelMapper.map(message1, MessageDTO.class);
        return  messageDTO;
    }

    @Override
    public List<MessageDTO> getChatHistory(String roomId) {
        String errorCode = "ChatServiceImpl:getChatHistory()";

        List<Message> messageList = messageRepo.findByChatIdOrderByTimeDesc(roomId).orElseThrow(()->new ResourceNotFoundException("Chat Room","roomId",roomId,errorCode));
        ModelMapper modelMapper = new ModelMapper();
        List<MessageDTO> messageDTOS = messageList.stream().map(x->modelMapper.map(x, MessageDTO.class)).collect(Collectors.toList());
        return messageDTOS;
    }

    @Override
    public void updateUserPresence(int userId, boolean status) {
        String errorCode = "ChatServiceImpl:updateUserPresence()";
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId,errorCode));
        user.setUserPresence(status);
        userRepo.save(user);
        ;
    }

    @Override
    public UserDTO getTheOtherUser(String roomId, int currentUserId) {
        String errorCode = "ChatServiceImpl:getTheOtherUser()";
        ChatRoom chatRoom = chatRoomRepo.findById(roomId).orElseThrow(()->new ResourceNotFoundException("Chat Room","roomId",roomId,errorCode));
        int otherUserId = chatRoom.getParticipantUserIds().stream().filter(e->!e.equals(currentUserId)).findFirst().orElseThrow(()->new ResourceNotFoundException("User","userId",currentUserId,errorCode));
        User otherUser = userRepo.findById(otherUserId).orElseThrow(()->new ResourceNotFoundException("User","Id",otherUserId,errorCode));
        UserDTO userDTO = userService.userToDTO(otherUser);
        return userDTO;
    }


}
