package com.athul.memegramspring.service.impl;

import com.athul.memegramspring.dto.NotificationsDTO;
import com.athul.memegramspring.entity.ChatRoom;
import com.athul.memegramspring.entity.Notifications;
import com.athul.memegramspring.entity.User;
import com.athul.memegramspring.enums.NotificationType;
import com.athul.memegramspring.exceptions.ResourceNotFoundException;
import com.athul.memegramspring.repository.ChatRoomRepo;
import com.athul.memegramspring.repository.NotificationRepo;
import com.athul.memegramspring.repository.UserRepo;
import com.athul.memegramspring.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final UserRepo userRepo;
    private final ChatRoomRepo chatRoomRepo;
    @Override
    public NotificationsDTO saveNotification(NotificationsDTO notificationsDTO) {
        String errorCode = "NotificationServiceImpl:saveNotification()";


        Notifications newNotification = new Notifications();
        newNotification.setNotTimeStamp(LocalDateTime.now());

        if(notificationsDTO.getNotificationType().equals(NotificationType.MESSAGE)){
            newNotification.setNotificationType(NotificationType.MESSAGE);
        }else if(notificationsDTO.getNotificationType().equals(NotificationType.AUDIOCALL)){
            newNotification.setNotificationType(NotificationType.AUDIOCALL);
        }else{
            newNotification.setNotificationType(NotificationType.VIDEOCALL);
        }

        //notificationFrom
        newNotification.setNotificationFrom(notificationsDTO.getNotificationFrom());

        int idOfFromUser = notificationsDTO.getNotificationFrom();
        User fromUser=userRepo.findById(idOfFromUser).orElseThrow(()->new ResourceNotFoundException("User","Id",idOfFromUser,errorCode));
        newNotification.setNotificationFromUserId(fromUser.getId());
        newNotification.setNotificationFromEmail(fromUser.getEmail());
        newNotification.setNotificationFromFullName(fromUser.getFullName());


        //notificationTo
        ChatRoom chatRoom = chatRoomRepo.findById(notificationsDTO.getChatRoomId()).orElseThrow(()->new ResourceNotFoundException("ChatRoom","Id",notificationsDTO.getChatRoomId(),errorCode));;
        int idOfToUser = chatRoom.getParticipantUserIds().stream().filter(e-> !e.equals(idOfFromUser)).findFirst().orElseThrow(()->new ResourceNotFoundException("Participants in Chat Room","Id",notificationsDTO.getChatRoomId(),errorCode));
        User toUser=userRepo.findById(idOfToUser).orElseThrow(()->new ResourceNotFoundException("User","Id",idOfFromUser,errorCode));
        newNotification.setNotificationTo(toUser.getId());
        newNotification.setNotificationToUserId(toUser.getId());
        newNotification.setNotificationToEmail(toUser.getEmail());
        newNotification.setNotificationToFullName(toUser.getFullName());


        newNotification.setRead(notificationsDTO.isRead());

        Notifications savedNotification = notificationRepo.save(newNotification);
        System.out.println("the saved notification in repo"+savedNotification);
        ModelMapper mapper = new ModelMapper();
        NotificationsDTO notificationsDTO1 = mapper.map(savedNotification,NotificationsDTO.class);
        System.out.println("the dto notification sending from service delete after checking once"+notificationsDTO1);

        return notificationsDTO1;
    }
}
