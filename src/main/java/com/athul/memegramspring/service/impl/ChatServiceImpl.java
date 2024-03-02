package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.ChatRoomDTO;
import com.athul.memegramspring.dto.MessageDTO;
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

    //create or retrieve if existing
    @Override
    public ChatRoomDTO createChatRoom(ChatRoom chatRoom) {
        String errorCode = "ChatServiceImpl: createChatRoom";
        List<Integer> participantUserIds = chatRoom.getParticipantUserIds();
        Collections.sort(participantUserIds);
        ChatRoom existingChatRoom = chatRoomRepo.findByParticipantUserIds(participantUserIds);
        existingChatRoom.setMessages(messageRepo.findByChatIdOrderByTimeDesc(existingChatRoom.getId()).orElseThrow(()->new ResourceNotFoundException("Chat Room","chat room Id",existingChatRoom.getId(),errorCode)));
        ModelMapper modelMapper = new ModelMapper();
        if (existingChatRoom !=null){
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
        if(message.getMessageType()!= MessageType.TEXT){
            //to upload image
        }
        Message message1 =messageRepo.save(message);
        System.out.println(message1);
        ModelMapper modelMapper = new ModelMapper();
        MessageDTO messageDTO = modelMapper.map(message, MessageDTO.class);
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
    public void updateUserPresence(String userId, boolean status) {
        String errorCode = "ChatServiceImpl:updateUserPresence()";
        System.out.println(Integer.valueOf(userId)+"check the userId convention, delete after checking once inside UpdateUserPresence in chat service Implementation");
        User user = userRepo.findById(Integer.valueOf(userId)).orElseThrow(()->new ResourceNotFoundException("User","Id",userId,errorCode));
        user.setUserPresence(status);
        userRepo.save(user);
        ;
    }


}
