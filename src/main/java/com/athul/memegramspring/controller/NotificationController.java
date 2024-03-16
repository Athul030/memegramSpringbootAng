package com.athul.memegramspring.controller;


import com.athul.memegramspring.dto.NotificationsDTO;
import com.athul.memegramspring.entity.Notifications;
import com.athul.memegramspring.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {


    private NotificationService notificationService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/app/notifications")
    @SendTo("/topic/notifications")
    public NotificationsDTO sendNotification(NotificationsDTO notificationsDTO){
//        simpMessagingTemplate.convertAndSend("/topic/notifications",notificationsDTO);
        System.out.println("From Notf controller"+notificationsDTO);
        NotificationsDTO savedNotificationDTO = notificationService.saveNotification(notificationsDTO);
        return savedNotificationDTO;

    }
}
