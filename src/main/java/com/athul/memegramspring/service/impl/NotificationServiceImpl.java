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
import java.util.List;
import java.util.stream.Collectors;

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

        newNotification.setChatRoomId(notificationsDTO.getChatRoomId());
        newNotification.setRead(notificationsDTO.isRead());

        Notifications savedNotification = notificationRepo.save(newNotification);
        ModelMapper mapper = new ModelMapper();
        NotificationsDTO notificationsDTO1 = mapper.map(savedNotification,NotificationsDTO.class);

        return notificationsDTO1;
    }

    @Override
    public List<NotificationsDTO> getAllNotificationsOfAUser(int userId) {
        String errorCode = "NotificationServiceImpl:getNotificationsAUser()";
        List<Notifications> notificationsList = notificationRepo.getAllByNotificationTo(userId).orElseThrow(()-> new ResourceNotFoundException("MessageNotifications","userId",userId,errorCode));
        ModelMapper mapper = new ModelMapper();
        System.out.println("the list of notification in repo"+notificationsList);
        List<NotificationsDTO> notificationsDTOList = notificationsList.stream().map(e->mapper.map(e, NotificationsDTO.class)).collect(Collectors.toList());
        System.out.println("the list of notification sending from service delete after checking once"+notificationsDTOList);

        return notificationsDTOList;
    }

    @Override
    public List<NotificationsDTO> getNotificationsOfAUserMessagesOnly(int userId) {
        String errorCode = "NotificationServiceImpl:getNotificationsAUserMessagesOnly()";
        List<Notifications> notificationsList = notificationRepo.fetchMessageNotificationsOfAUser(userId, NotificationType.MESSAGE).orElseThrow(()-> new ResourceNotFoundException("MessageNotifications","userId",userId,errorCode));
        ModelMapper mapper1 = new ModelMapper();
        System.out.println("the list of notification in repo"+notificationsList);
        List<NotificationsDTO> notificationsDTOList = notificationsList.stream().map(e->mapper1.map(e, NotificationsDTO.class)).collect(Collectors.toList());
        System.out.println("the list of notification sending from service delete after checking once"+notificationsDTOList);

        return notificationsDTOList;
    }

    @Override
    public boolean notificationIsRead(int notId) {
        String errorCode = "NotificationServiceImpl:notificationIsRead()";
        Notifications notification = notificationRepo.findById(notId).orElseThrow(()-> new ResourceNotFoundException("MessageNotifications","NotificationId",notId,errorCode));
        notification.setRead(true);
        Notifications notifications = notificationRepo.save(notification);
        if(notifications!=null) return true;
        else return false;
    }

    @Override
    public boolean deleteNotification(int notId) {
        String errorCode = "NotificationServiceImpl:deleteNotification()";
        Notifications notification = notificationRepo.findById(notId).orElseThrow(()-> new ResourceNotFoundException("MessageNotifications","NotificationId",notId,errorCode));
        notificationRepo.delete(notification);
        boolean result = notificationRepo.existsById(notId);
        return !result;
    }

    @Override
    public boolean setChatNotificationStatus(String roomId) {
        try {
            String errorCode = "NotificationServiceImpl:setChatNotificationStatus()";
            List<Notifications> notificationsList = notificationRepo.getAllByChatRoomId(roomId).orElseThrow(() -> new ResourceNotFoundException("MessageNotifications", "ChatRoomId", roomId, errorCode));
            notificationsList.stream().forEach(e -> e.setRead(true));
            notificationRepo.saveAll(notificationsList);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
}
