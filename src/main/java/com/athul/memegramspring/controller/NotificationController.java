package com.athul.memegramspring.controller;


import com.athul.memegramspring.dto.NotificationsDTO;
import com.athul.memegramspring.entity.Notifications;
import com.athul.memegramspring.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {


    private final NotificationService notificationService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/notifications")
    @SendTo("/topic/notifications")
    public NotificationsDTO sendNotification(NotificationsDTO notificationsDTO){
//        simpMessagingTemplate.convertAndSend("/topic/notifications",notificationsDTO);
        System.out.println("From Notf controller"+notificationsDTO);
        NotificationsDTO savedNotificationDTO = notificationService.saveNotification(notificationsDTO);
        return savedNotificationDTO;

    }

    //video call
    @MessageMapping("/videoCall")
    @SendTo("/topic/videoCallTo")
    public NotificationsDTO sendVideoCallNotification(NotificationsDTO notificationsDTO){
        System.out.println("sendVideoCallNotf"+notificationsDTO.getVideoCallRoomId());
        System.out.println("sendVideoCallNotf"+notificationsDTO.getNotificationType());
        System.out.println("sendVideoCallNotf"+notificationsDTO.getNotificationFrom());
        System.out.println("sendVideoCallNotf"+notificationsDTO.isRead());
        return notificationsDTO;
    }

    @GetMapping("/messageNotify/{userId}")
    public ResponseEntity<List<NotificationsDTO>>  getAllMessageNotifications(@PathVariable int userId){
        List<NotificationsDTO> messageNotificationsDTOList = notificationService.getNotificationsOfAUserMessagesOnly(userId);
        return new ResponseEntity<>(messageNotificationsDTOList, HttpStatus.OK);
    }

    @GetMapping("/allNotify/{userId}")
    public ResponseEntity<List<NotificationsDTO>> getAllNotifications(@PathVariable int userId){
        List<NotificationsDTO> allNotificationsDTOList = notificationService.getAllNotificationsOfAUser(userId);
        return new ResponseEntity<>(allNotificationsDTOList, HttpStatus.OK);
    }


    @PatchMapping("/notificationsRead/{notId}")
    public ResponseEntity<Boolean> notificationIsRead(@PathVariable int notId){
        boolean result = notificationService.notificationIsRead(notId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/notifications/{notId}")
    public ResponseEntity<Boolean> notificationDelete(@PathVariable int notId){
        boolean result = notificationService.notificationIsRead(notId);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


}
