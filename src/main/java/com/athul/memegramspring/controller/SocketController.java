package com.athul.memegramspring.controller;

import com.athul.memegramspring.dto.*;
import com.athul.memegramspring.entity.ChatRoom;
import com.athul.memegramspring.entity.ChatMessage;
import com.athul.memegramspring.entity.Message;
import com.athul.memegramspring.service.ChatService;
import com.athul.memegramspring.service.FileService;
import com.athul.memegramspring.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final ChatService chatService;

    private final FileService fileService;

    private final UserService userService;

    @Value("${project.image}")
    private String path;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public MessageDTO chat(@DestinationVariable String roomId, Message message){
        System.out.println(message);
        MessageDTO messageDTO = chatService.saveMessage(message);
        return messageDTO;
    }

//    @MessageMapping("/video/{roomId}")
//    @SendTo("/topic/video/{roomId}")
//    public VideoCallDTO handleVideoCall(VideoCallDTO videoCallDTO){
//        return videoCallDTO;
//    }


    @PostMapping("/createChatRoom")
    public ResponseEntity<ChatRoomDTO> createChatRoom(@RequestBody ChatRoom request){
    ChatRoomDTO chatRoomDTO = chatService.createChatRoom(request);
        System.out.println(chatRoomDTO);
        return new ResponseEntity<>(chatRoomDTO, HttpStatus.OK);
    }

    @GetMapping("/chat/{roomId}/history")
    public ResponseEntity<List<MessageDTO>> getChatHistory(@PathVariable String roomId){
        List<MessageDTO> chatHistory = chatService.getChatHistory(roomId);
        return ResponseEntity.ok(chatHistory);
    }

    @GetMapping("/chat/history")
    public ResponseEntity<List<MessageDTO>> getChatHistories(@RequestParam String id){
        List<MessageDTO> allMessages = chatService.getChatHistory(id);
        return ResponseEntity.ok(allMessages);
    }



    //user presence : handle later

    @PostMapping("/userPresence/{userId}/{status}")
    public ResponseEntity<Void> updateUserPresence(@PathVariable int userId,@PathVariable boolean status){
        chatService.updateUserPresence(userId,status);
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/chat/{roomId}/typing/{userId}")
    public TypingIndicatorDTO typing(@DestinationVariable String roomId, @DestinationVariable String userId){
        TypingIndicatorDTO typingIndicatorDTO = new TypingIndicatorDTO(userId,true);
        return  typingIndicatorDTO;
    }

    //upload image




}
